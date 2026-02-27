package com.carlos.eventpulse.features.chat.domain.model

data class Message(
    val id: String,
    val eventoId: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val rolUsuario: String,
    val contenido: String,
    val enviadoEn: String,
    val cardType: String? = null,
    val cardId: String? = null,
    val cardTitle: String? = null
)
