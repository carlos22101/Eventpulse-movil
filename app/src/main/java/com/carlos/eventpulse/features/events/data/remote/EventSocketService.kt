package com.carlos.eventpulse.features.events.data.remote

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.carlos.eventpulse.core.di.PublicClient
import com.carlos.eventpulse.core.util.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

// --- WebSocket Event model ---
data class WsEvent(
    @SerializedName("tipo") val tipo: String,
    @SerializedName("payload") val payload: JsonObject?,
    @SerializedName("evento_id") val eventoId: String?
)

@Singleton
class EventSocketService @Inject constructor(
    @PublicClient private val okHttpClient: OkHttpClient
) {
    private val gson = Gson()
    private var webSocket: WebSocket? = null
    private var currentToken: String? = null

    // Aumentamos replay a 1 para que nuevos suscriptores reciban el último evento si es necesario
    private val _events = MutableSharedFlow<WsEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val events: SharedFlow<WsEvent> = _events.asSharedFlow()

    private val _connectionState = MutableSharedFlow<ConnectionState>(
        replay = 1,
        extraBufferCapacity = 8
    )
    val connectionState: SharedFlow<ConnectionState> = _connectionState.asSharedFlow()

    enum class ConnectionState { CONNECTING, CONNECTED, DISCONNECTED, FAILED }

    fun connect(token: String) {
        if (webSocket != null && currentToken == token) return
        currentToken = token
        disconnect()
        val url = "${Constants.WS_BASE_URL}$token"
        val request = Request.Builder().url(url).build()
        Log.d("EventSocketService", "Connecting to WebSocket: $url")
        _connectionState.tryEmit(ConnectionState.CONNECTING)
        webSocket = okHttpClient.newWebSocket(request, EventWebSocketListener())
    }

    fun disconnect() {
        webSocket?.close(1000, "Cerrando conexión")
        webSocket = null
    }

    private inner class EventWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("EventSocketService", "WebSocket conectado")
            _connectionState.tryEmit(ConnectionState.CONNECTED)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("EventSocketService", "WS mensaje: $text")
            try {
                val event = gson.fromJson(text, WsEvent::class.java)
                if (event.tipo == Constants.WS_PING) return

                // Usamos emit en lugar de tryEmit para asegurar que no se pierda nada si el buffer se llena
                // Pero como estamos en un callback de OkHttp, lo ideal es lanzarlo en un scope o usar tryEmit con precaución
                val success = _events.tryEmit(event)
                if (!success) {
                    Log.w("EventSocketService", "Buffer de eventos lleno, evento perdido: ${event.tipo}")
                }
            } catch (e: Exception) {
                Log.e("EventSocketService", "Error parsing WS message", e)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("EventSocketService", "WebSocket fallo: ${t.message}")
            _connectionState.tryEmit(ConnectionState.FAILED)
            scheduleReconnect()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            _connectionState.tryEmit(ConnectionState.DISCONNECTED)
        }
    }

    private fun scheduleReconnect() {
        Handler(Looper.getMainLooper()).postDelayed({
            currentToken?.let { connect(it) }
        }, 3000)
    }
}