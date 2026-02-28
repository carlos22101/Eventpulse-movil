package com.carlos.eventpulse.features.chat.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val eventoId: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val rolUsuario: String,
    val contenido: String,
    val enviadoEn: String,
    // Embedded card references
    val cardType: String? = null,  // "incidencia" | "tarea" | null
    val cardId: String? = null,
    val cardTitle: String? = null
)

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY enviadoEn ASC")
    fun observeMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
}
