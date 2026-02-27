package com.carlos.eventpulse.features.chat.data.repositories

import android.util.Log
import com.carlos.eventpulse.core.util.Constants
import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.chat.data.local.MessageDao
import com.carlos.eventpulse.features.chat.data.local.MessageEntity
import com.carlos.eventpulse.features.chat.data.remote.ChatApiService
import com.carlos.eventpulse.features.chat.data.remote.MessageDto
import com.carlos.eventpulse.features.chat.data.remote.SendMessageRequest
import com.carlos.eventpulse.features.chat.domain.model.Message
import com.carlos.eventpulse.features.chat.domain.repositories.ChatRepository
import com.carlos.eventpulse.features.events.data.remote.EventSocketService
import com.carlos.eventpulse.features.events.data.remote.IncidentDto
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val apiService: ChatApiService,
    private val dao: MessageDao,
    private val socketService: EventSocketService
) : ChatRepository {

    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val TAG = "ChatRepository"

    init {
        observeWebSocketMessages()
    }

    private fun observeWebSocketMessages() {
        scope.launch {
            socketService.events.collect { wsEvent ->
                Log.d(TAG, "WS Evento recibido: ${wsEvent.tipo}")
                when (wsEvent.tipo) {
                    Constants.WS_MENSAJE_NUEVO -> {
                        wsEvent.payload?.let {
                            val dto = gson.fromJson(it, MessageDto::class.java)
                            dao.insertMessage(dto.toEntity())
                        }
                    }
                    Constants.WS_INCIDENCIA_NUEVA, Constants.WS_INCIDENCIA_ACTUALIZADA -> {
                        wsEvent.payload?.let {
                            val dto = gson.fromJson(it, IncidentDto::class.java)
                            // Convertimos la incidencia en un mensaje especial tipo "card" para el chat
                            val cardMsg = MessageEntity(
                                id = dto.id + "_" + System.currentTimeMillis(),
                                eventoId = dto.eventoId,
                                usuarioId = "system",
                                nombreUsuario = "Sistema",
                                rolUsuario = "admin",
                                contenido = "Incidencia: ${dto.descripcion}",
                                enviadoEn = dto.actualizadaEn ?: dto.creadaEn,
                                cardType = "incidencia",
                                cardId = dto.id,
                                cardTitle = "${dto.tipo.uppercase()} - ${dto.estado.uppercase()}"
                            )
                            dao.insertMessage(cardMsg)
                            Log.d(TAG, "Incidencia guardada como tarjeta en el chat")
                        }
                    }
                }
            }
        }
    }

    override fun observeMessages(): Flow<List<Message>> =
        dao.observeMessages().map { list -> list.map { it.toDomain() } }

    override suspend fun loadHistorial(): Resource<List<Message>> {
        return try {
            val remote = apiService.getHistorial()
            dao.insertMessages(remote.map { it.toEntity() })
            Resource.Success(remote.map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al cargar historial")
        }
    }

    override suspend fun sendMessage(contenido: String): Resource<Message> {
        return try {
            val dto = apiService.sendMensaje(SendMessageRequest(contenido))
            dao.insertMessage(dto.toEntity())
            Resource.Success(dto.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al enviar mensaje")
        }
    }
}

// --- Mappers ---
private fun MessageDto.toEntity() = MessageEntity(
    id = id, eventoId = eventoId, usuarioId = usuarioId,
    nombreUsuario = nombreUsuario, rolUsuario = rolUsuario,
    contenido = contenido, enviadoEn = enviadoEn
)

private fun MessageEntity.toDomain() = Message(
    id = id, eventoId = eventoId, usuarioId = usuarioId,
    nombreUsuario = nombreUsuario, rolUsuario = rolUsuario,
    contenido = contenido, enviadoEn = enviadoEn,
    cardType = cardType, cardId = cardId, cardTitle = cardTitle
)

private fun MessageDto.toDomain() = Message(
    id = id, eventoId = eventoId, usuarioId = usuarioId,
    nombreUsuario = nombreUsuario, rolUsuario = rolUsuario,
    contenido = contenido, enviadoEn = enviadoEn
)
