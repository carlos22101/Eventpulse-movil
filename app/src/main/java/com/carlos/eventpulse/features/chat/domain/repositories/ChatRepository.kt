package com.carlos.eventpulse.features.chat.domain.repositories

import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeMessages(): Flow<List<Message>>
    suspend fun loadHistorial(): Resource<List<Message>>
    suspend fun sendMessage(contenido: String): Resource<Message>
}
