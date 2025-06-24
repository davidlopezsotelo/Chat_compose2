
package com.dls.chatcompose2.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.use_case.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gestiona la lógica de registro de usuarios.
 * Se conecta con RegisterUseCase y expone el estado de la UI mediante un StateFlow.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    init {
        Log.d("RegisterViewModel", "✅ RegisterViewModel creado correctamente")
    }

    /**
     * Inicia el proceso de registro con los datos proporcionados.
     * Actualiza el estado de la UI según el resultado.
     */
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            Log.d("RegisterViewModel", "🔄 Intentando registrar usuario con email: $email")

            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

            val result = registerUseCase(name, email, password)

            result.fold(
                onSuccess = {
                    Log.d("RegisterViewModel", "✅ Registro exitoso para: $email")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    val errorMsg = error.localizedMessage ?: "❌ Error desconocido"
                    Log.e("RegisterViewModel", "⛔ Error al registrar: $errorMsg")
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            )
        }
    }

    /**
     * Restablece el estado de la UI a su valor por defecto.
     */
    fun resetState() {
        Log.d("RegisterViewModel", "♻️ Reiniciando estado de UI")
        _uiState.value = RegisterUiState()
    }
}

