package com.dls.chatcompose2.presentation.register


/**
 * Representa el estado de la interfaz de usuario en la pantalla de registro.
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
