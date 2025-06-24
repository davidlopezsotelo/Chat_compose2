package com.dls.chatcompose2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.repository.AuthRepository
import com.dls.chatcompose2.domain.use_case.auth.LoginUseCase
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel para gestionar la lógica del login con email y contraseña.
 * Usa corutinas para ejecutar el LoginUseCase sin bloquear el hilo principal.
 */

@Suppress("DEPRECATION")
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    /**
     * Inicia sesión usando correo electrónico y contraseña
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "Iniciando sesión con email: $email")
            _loginState.value = LoginState(isLoading = true)

            val result = loginUseCase(email, password)
            _loginState.value = if (result.isSuccess) {
                Log.d("LoginViewModel", "Login con email exitoso")
                LoginState(success = true)
            } else {
                Log.e("LoginViewModel", "Login fallido: ${result.exceptionOrNull()?.message}")
                LoginState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Recibe el intent del SignIn de Google y ejecuta el login
     */
    fun onGoogleSignInResult(credential: SignInCredential) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "Procesando resultado de Google Sign-In")
            _loginState.value = LoginState(isLoading = true)

            val result = authRepository.signInWithGoogle(credential)
            _loginState.value = if (result.isSuccess) {
                Log.d("LoginViewModel", "Login con Google exitoso")
                LoginState(success = true)
            } else {
                Log.e("LoginViewModel", "Login con Google fallido: ${result.exceptionOrNull()?.message}")
                LoginState(error = result.exceptionOrNull()?.message)
            }
        }
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

