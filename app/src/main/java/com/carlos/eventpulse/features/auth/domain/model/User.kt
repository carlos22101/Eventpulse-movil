package com.carlos.eventpulse.features.auth.domain.model

data class User(
    val id: String,
    val nombreUsuario: String,
    val nombre: String,
    val rol: String,
    val eventoId: String?,
    val activo: Boolean,
    val creadoEn: String
)