package com.dls.chatcompose2.domain.use_case.auth

import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.AuthRepository

/**
 * Caso de uso para registrar un nuevo usuario con nombre, email y contraseña.
 */
class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        val user = User(name = name, email = email)
        return repository.registerUser(name, email, password).onSuccess {
            repository.saveUserToFirestore(user)
        }
    }
}
