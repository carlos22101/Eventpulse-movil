package com.carlos.eventpulse.features.events.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

// --- DTOs ---

data class IncidentDto(
    @SerializedName("id") val id: String,
    @SerializedName("evento_id") val eventoId: String,
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("zona_nombre") val zonaNombre: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("creada_por") val creadaPor: String,
    @SerializedName("asignada_a") val asignadaA: String?,
    @SerializedName("nombre_asignado") val nombreAsignado: String?,
    @SerializedName("creada_en") val creadaEn: String,
    @SerializedName("actualizada_en") val actualizadaEn: String
)

data class TaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("evento_id") val eventoId: String,
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("zona_nombre") val zonaNombre: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("prioridad") val prioridad: String,
    @SerializedName("creada_por") val creadaPor: String,
    @SerializedName("asignada_a") val asignadaA: String?,
    @SerializedName("nombre_asignado") val nombreAsignado: String?,
    @SerializedName("completada_en") val completadaEn: String?,
    @SerializedName("creada_en") val creadaEn: String
)

data class ZoneDto(
    @SerializedName("id") val id: String,
    @SerializedName("evento_id") val eventoId: String,
    @SerializedName("nombre") val nombre: String
)

data class CreateIncidentRequest(
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("asignada_a") val asignadaA: String?
)

data class UpdateIncidentRequest(
    @SerializedName("estado") val estado: String?,
    @SerializedName("asignada_a") val asignadaA: String?
)

data class CreateTaskRequest(
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("prioridad") val prioridad: String,
    @SerializedName("asignada_a") val asignadaA: String?
)

data class UpdateTaskRequest(
    @SerializedName("estado") val estado: String?,
    @SerializedName("asignada_a") val asignadaA: String?
)

// --- API Service ---

interface EventApiService {
    @GET("incidencias")
    suspend fun getIncidencias(): List<IncidentDto>

    @GET("incidencias/{id}")
    suspend fun getIncidencia(@Path("id") id: String): IncidentDto

    @POST("incidencias")
    suspend fun createIncidencia(@Body request: CreateIncidentRequest): IncidentDto

    @PATCH("incidencias/{id}")
    suspend fun updateIncidencia(
        @Path("id") id: String,
        @Body request: UpdateIncidentRequest
    ): IncidentDto

    @GET("tareas")
    suspend fun getTareas(): List<TaskDto>

    @GET("tareas/{id}")
    suspend fun getTarea(@Path("id") id: String): TaskDto

    @POST("tareas")
    suspend fun createTarea(@Body request: CreateTaskRequest): TaskDto

    @PATCH("tareas/{id}")
    suspend fun updateTarea(
        @Path("id") id: String,
        @Body request: UpdateTaskRequest
    ): TaskDto

    @GET("zonas")
    suspend fun getZonas(): List<ZoneDto>
}