// presentation/register/RegisterViewModel.kt
package com.dls.chatcompose2.presentation.register


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.use_case.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



/**
 * ViewModel que gestiona la lógica de registro de usuarios.
 * Se conecta con el caso de uso RegisterUseCase y expone el estado de la UI mediante un StateFlow.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    // Estado interno mutable de la pantalla de registro
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    init {
        Log.d("RegisterViewModel", "RegisterViewModel creado correctamente")
    }

    /**
     * Inicia el proceso de registro con los datos proporcionados.
     * @param name Nombre del usuario
     * @param email Correo electrónico
     * @param password Contraseña
     */
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            Log.d("RegisterViewModel", "Intentando registrar usuario con email: $email")
            _uiState.value = RegisterUiState(isLoading = true)

            val result = registerUseCase(name, email, password)

            _uiState.value = if (result.isSuccess) {
                Log.d("RegisterViewModel", "Registro exitoso para: $email")
                RegisterUiState(isSuccess = true)
            } else {
                val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
                Log.e("RegisterViewModel", "Error al registrar: $errorMsg")
                RegisterUiState(errorMessage = errorMsg)
            }
        }
    }

    /**
     * Restablece el estado de la UI al valor inicial (útil después de un registro o para reiniciar errores).
     */
    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}

