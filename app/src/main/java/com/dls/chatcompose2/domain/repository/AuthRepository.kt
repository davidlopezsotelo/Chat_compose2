@file:Suppress("DEPRECATION")

package com.dls.chatcompose2.domain.repository

import com.dls.chatcompose2.domain.model.User
import com.google.android.gms.auth.api.identity.SignInCredential

/**
 * Interfaz del repositorio de autenticación.
 * Define todas las operaciones relacionadas con Firebase Auth, Firestore y Google Sign-In.
 * Su implementación se encuentra en AuthRepositoryImpl.
 */
interface AuthRepository {

    /**
     * Inicia sesión con correo y contraseña.
     * @return Result con éxito o error.
     */
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<Unit>

    /**
     * Registra un nuevo usuario con correo, contraseña y nombre.
     * @return Result con éxito o error.
     */
    suspend fun registerUser(name: String, email: String, password: String): Result<Unit>

    /**
     * Cierra la sesión del usuario actual.
     */
    suspend fun logout()

    /**
     * Devuelve el ID del usuario actualmente autenticado, si existe.
     */
    fun getCurrentUserId(): String?

    /**
     * Guarda un objeto de tipo User en Firestore, sobrescribiendo si ya existe.
     * @return Result con éxito o error.
     */
    suspend fun saveUserToFirestore(user: User): Result<Unit>

    /**
     * Inicia sesión con una cuenta de Google a través del SignInCredential.
     * @return Result con éxito o error.
     */
    suspend fun signInWithGoogle(credential: SignInCredential): Result<Unit>


}
