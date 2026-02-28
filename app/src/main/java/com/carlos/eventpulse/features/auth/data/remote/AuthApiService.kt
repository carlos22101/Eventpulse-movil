package com.carlos.eventpulse.features.auth.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// --- DTOs ---

data class LoginRequest(
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("password_hash") val passwordHash: String
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("evento_id") val eventoId: String?,
    @SerializedName("activo") val activo: Boolean,
    @SerializedName("creado_en") val creadoEn: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("usuario") val usuario: UserDto
)

// --- API Service ---

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getMe(): UserDto
}