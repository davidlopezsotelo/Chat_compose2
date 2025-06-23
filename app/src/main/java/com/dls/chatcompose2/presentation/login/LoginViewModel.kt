// presentation/login/LoginViewModel.kt
package com.dls.chatcompose2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para manejar la lógica de la pantalla de login.
 * Aquí se añadirá más adelante la lógica de autenticación con email/password o Google.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    // Aquí puedes inyectar tus casos de uso (loginUseCase, etc)
) : ViewModel() {

    init {
        Log.d("LoginViewModel", "LoginViewModel creado correctamente")
    }

    // TODO: Añadir funciones como loginUser(email, password), handleGoogleSignIn, etc.
}
