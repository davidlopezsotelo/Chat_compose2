package com.dls.chatcompose2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.use_case.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar la lógica del login con email y contraseña.
 * Usa corutinas para ejecutar el LoginUseCase sin bloquear el hilo principal.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    /**
     * Inicia el proceso de login usando corutinas y actualiza el estado de la UI.
     */
    fun login(email: String, password: String) {
        Log.d("LoginViewModel", "Intentando iniciar sesión con email: $email")
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            val result = loginUseCase(email, password)

            _loginState.value = if (result.isSuccess) {
                Log.d("LoginViewModel", "Login exitoso")
                LoginState(success = true)
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                Log.e("LoginViewModel", "Error de login: $error")
                LoginState(error = error)
            }
        }
    }

    /**
     * Resetea el estado de la interfaz de usuario.
     */
    fun resetState() {
        _loginState.value = LoginState()
    }
}

/**
 * Estado del login usado para reflejar el progreso, errores y éxito.
 */
data class LoginState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

