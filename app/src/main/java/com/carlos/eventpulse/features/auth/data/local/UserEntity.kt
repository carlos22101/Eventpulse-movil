package com.carlos.eventpulse.features.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val nombreUsuario: String,
    val nombre: String,
    val rol: String,
    val eventoId: String?,
    val activo: Boolean,
    val creadoEn: String,
    val token: String
)