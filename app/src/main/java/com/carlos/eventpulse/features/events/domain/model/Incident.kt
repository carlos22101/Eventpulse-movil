package com.carlos.eventpulse.features.events.domain.model

data class Incident(
    val id: String,
    val eventoId: String,
    val zonaId: String,
    val zonaNombre: String,
    val tipo: String,
    val descripcion: String,
    val estado: String,
    val creadaPor: String,
    val asignadaA: String?,
    val nombreAsignado: String?,
    val creadaEn: String,
    val actualizadaEn: String
)

data class Task(
    val id: String,
    val eventoId: String,
    val zonaId: String,
    val zonaNombre: String,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val prioridad: String,
    val creadaPor: String,
    val asignadaA: String?,
    val nombreAsignado: String?,
    val completadaEn: String?,
    val creadaEn: String
)

data class Zone(
    val id: String,
    val eventoId: String,
    val nombre: String
)