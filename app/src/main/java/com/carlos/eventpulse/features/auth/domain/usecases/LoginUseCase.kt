package com.carlos.eventpulse.features.auth.domain.usecases

import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.auth.domain.model.User
import com.carlos.eventpulse.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(nombreUsuario: String, password: String): Resource<User> {
        if (nombreUsuario.isBlank()) return Resource.Error("El usuario no puede estar vacío")
        if (password.isBlank()) return Resource.Error("La contraseña no puede estar vacía")
        return repository.login(nombreUsuario, password)
    }
}