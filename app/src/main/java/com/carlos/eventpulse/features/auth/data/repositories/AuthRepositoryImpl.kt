package com.carlos.eventpulse.features.auth.data.repositories

import android.content.SharedPreferences
import com.carlos.eventpulse.core.util.Constants
import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.auth.data.remote.AuthApiService
import com.carlos.eventpulse.features.auth.data.remote.LoginRequest
import com.carlos.eventpulse.features.auth.data.remote.UserDto
import com.carlos.eventpulse.features.auth.domain.model.User
import com.carlos.eventpulse.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {

    override suspend fun login(nombreUsuario: String, password: String): Resource<User> {
        return try {
            val response = apiService.login(LoginRequest(nombreUsuario, password))
            sharedPreferences.edit().apply {
                putString(Constants.KEY_TOKEN, response.token)
                putString(Constants.KEY_USER_ID, response.usuario.id)
                putString(Constants.KEY_USER_ROL, response.usuario.rol)
                putString(Constants.KEY_EVENTO_ID, response.usuario.eventoId)
            }.apply()
            Resource.Success(response.usuario.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de autenticación")
        }
    }

    override suspend fun getMe(): Resource<User> {
        return try {
            Resource.Success(apiService.getMe().toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener usuario")
        }
    }

    override fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    override fun getToken(): String? =
        sharedPreferences.getString(Constants.KEY_TOKEN, null)

    override fun getUserRol(): String? =
        sharedPreferences.getString(Constants.KEY_USER_ROL, null)

    override fun getEventoId(): String? =
        sharedPreferences.getString(Constants.KEY_EVENTO_ID, null)
}

private fun UserDto.toDomain() = User(
    id = id,
    nombreUsuario = nombreUsuario,
    nombre = nombre,
    rol = rol,
    eventoId = eventoId,
    activo = activo,
    creadoEn = creadoEn
)