@file:Suppress("DEPRECATION")

package com.dls.chatcompose2.domain.use_case.auth

import com.dls.chatcompose2.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Caso de uso para iniciar sesión con email y contraseña.
 * Llama al repositorio para autenticar al usuario.
 */
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Ejecuta el inicio de sesión en un hilo de I/O para evitar bloqueo del hilo principal.
     *
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return Resultado de la operación, con éxito o fallo
     */
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            repository.loginWithEmailAndPassword(email, password)
        }
    }

    suspend fun signInWithGoogle(credential: SignInCredential): Result<Unit> {
        return repository.signInWithGoogle(credential)
    }

}




