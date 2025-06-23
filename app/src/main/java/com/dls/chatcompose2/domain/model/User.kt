package com.dls.chatcompose2.domain.model
/**
 * Modelo de dominio para representar un usuario de la aplicación.
 *
 * Esta clase se usa para comunicar datos de usuario entre capas (UI, domain, data)
 * y para almacenar la información del usuario en Firestore.
 */
data class User (
    val uid: String = "",              // ID único del usuario (UID de Firebase Auth)
    val name: String = "",             // Nombre completo del usuario
    val email: String = "",            // Dirección de correo electrónico
    val profilePictureUrl: String? = null // URL de la foto de perfil, puede ser nula
)