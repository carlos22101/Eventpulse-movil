package com.carlos.eventpulse.core.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.carlos.eventpulse.MainActivity
import com.carlos.eventpulse.core.util.Constants
import com.carlos.eventpulse.features.events.data.remote.EventSocketService
import com.carlos.eventpulse.features.events.data.remote.IncidentDto
import com.carlos.eventpulse.features.events.data.remote.TaskDto
import com.carlos.eventpulse.features.chat.data.remote.MessageDto
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class EventPulseWebSocketService : Service() {

    @Inject lateinit var socketService: EventSocketService

    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isChatVisible = false
    private val TAG = "EventPulseWS"

    companion object {
        const val ACTION_CHAT_VISIBLE = "action_chat_visible"
        const val ACTION_CHAT_HIDDEN = "action_chat_hidden"
        const val FOREGROUND_NOTIF_ID = 999

        fun start(context: Context, token: String) {
            val intent = Intent(context, EventPulseWebSocketService::class.java)
                .putExtra("token", token)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(intent)
            else
                context.startService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, EventPulseWebSocketService::class.java))
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        startForeground(FOREGROUND_NOTIF_ID, buildForegroundNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CHAT_VISIBLE -> isChatVisible = true
            ACTION_CHAT_HIDDEN -> isChatVisible = false
            else -> {
                val token = intent?.getStringExtra("token") ?: return START_STICKY
                socketService.connect(token)
                listenToEvents()
            }
        }
        return START_STICKY
    }

    private fun listenToEvents() {
        scope.launch {
            socketService.events.collect { wsEvent ->
                Log.d(TAG, "Evento recibido en Service: ${wsEvent.tipo}")
                when (wsEvent.tipo) {
                    Constants.WS_MENSAJE_NUEVO -> {
                        if (!isChatVisible) {
                            wsEvent.payload?.let {
                                val dto = gson.fromJson(it, MessageDto::class.java)
                                showChatNotification(dto.nombreUsuario, dto.contenido)
                            }
                        }
                    }
                    Constants.WS_INCIDENCIA_NUEVA, Constants.WS_INCIDENCIA_ACTUALIZADA -> {
                        wsEvent.payload?.let {
                            val dto = gson.fromJson(it, IncidentDto::class.java)
                            val titulo = if (wsEvent.tipo == Constants.WS_INCIDENCIA_ACTUALIZADA)
                                "🚨 Incidencia Actualizada" else "🚨 Nueva Incidencia"
                            showIncidentNotification(dto.id, "$titulo: ${dto.tipo}", dto.descripcion)
                        }
                    }
                    Constants.WS_TAREA_NUEVA, Constants.WS_TAREA_ACTUALIZADA -> {
                        wsEvent.payload?.let {
                            val dto = gson.fromJson(it, TaskDto::class.java)
                            showTaskNotification(dto.id, dto.titulo)
                        }
                    }
                }
            }
        }
    }

    // --- Notification builders ---

    private fun buildDeepLinkIntent(type: String, id: String? = null): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(Constants.DEEP_LINK_TYPE, type)
            id?.let { putExtra(Constants.DEEP_LINK_ID, it) }
        }
        return PendingIntent.getActivity(
            this, id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showChatNotification(sender: String, message: String) {
        if (!hasNotificationPermission()) return
        val notif = NotificationCompat.Builder(this, Constants.CHANNEL_CHAT)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Mensaje de $sender")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(buildDeepLinkIntent(Constants.DEEP_LINK_CHAT))
            .build()
        notificationManager().notify(Constants.NOTIF_CHAT_ID, notif)
    }

    private fun showIncidentNotification(id: String, tipo: String, desc: String) {
        if (!hasNotificationPermission()) return
        val notif = NotificationCompat.Builder(this, Constants.CHANNEL_INCIDENCIAS)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(tipo)
            .setContentText(desc)
            .setAutoCancel(true)
            .setContentIntent(buildDeepLinkIntent(Constants.DEEP_LINK_INCIDENCIA, id))
            .build()
        notificationManager().notify(Constants.NOTIF_INCIDENCIA_ID, notif)
    }

    private fun showTaskNotification(id: String, titulo: String) {
        if (!hasNotificationPermission()) return
        val notif = NotificationCompat.Builder(this, Constants.CHANNEL_TAREAS)
            .setSmallIcon(android.R.drawable.ic_menu_agenda)
            .setContentTitle("📋 Tarea: $titulo")
            .setAutoCancel(true)
            .setContentIntent(buildDeepLinkIntent(Constants.DEEP_LINK_TAREA, id))
            .build()
        notificationManager().notify(Constants.NOTIF_TAREA_ID, notif)
    }

    private fun buildForegroundNotification() =
        NotificationCompat.Builder(this, Constants.CHANNEL_CHAT)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("EventPulse")
            .setContentText("Conectado en tiempo real")
            .setSilent(true)
            .setOngoing(true)
            .build()

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = notificationManager()
            listOf(
                NotificationChannel(Constants.CHANNEL_CHAT, "Chat", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel(Constants.CHANNEL_INCIDENCIAS, "Incidencias", NotificationManager.IMPORTANCE_HIGH),
                NotificationChannel(Constants.CHANNEL_TAREAS, "Tareas", NotificationManager.IMPORTANCE_DEFAULT)
            ).forEach { nm.createNotificationChannel(it) }
        }
    }

    private fun notificationManager() =
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        socketService.disconnect()
    }
}
