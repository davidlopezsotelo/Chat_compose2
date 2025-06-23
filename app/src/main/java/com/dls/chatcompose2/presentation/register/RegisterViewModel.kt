// presentation/register/RegisterViewModel.kt
package com.dls.chatcompose2.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para manejar la lógica de la pantalla de registro.
 * Se encargará de registrar usuarios en Firebase Auth y guardar sus datos.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    // Inyectar los casos de uso o repositorios cuando estén listos

) : ViewModel() {

    init {
        Log.d("RegisterViewModel", "RegisterViewModel creado correctamente")
    }

    // TODO: Añadir funciones como registerUser(name, email, password), etc.
}
