package com.carlos.eventpulse.features.auth.domain.repositories

import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.auth.domain.model.User

interface AuthRepository {
    suspend fun login(nombreUsuario: String, password: String): Resource<User>
    suspend fun getMe(): Resource<User>
    fun logout()
    fun getToken(): String?
    fun getUserRol(): String?
    fun getEventoId(): String?
}