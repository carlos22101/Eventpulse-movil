package com.carlos.eventpulse.features.chat.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class MessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("evento_id") val eventoId: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("rol_usuario") val rolUsuario: String,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("enviado_en") val enviadoEn: String
)

data class SendMessageRequest(
    @SerializedName("contenido") val contenido: String
)

interface ChatApiService {
    @GET("chat/historial")
    suspend fun getHistorial(): List<MessageDto>

    @POST("chat/mensaje")
    suspend fun sendMensaje(@Body request: SendMessageRequest): MessageDto
}
