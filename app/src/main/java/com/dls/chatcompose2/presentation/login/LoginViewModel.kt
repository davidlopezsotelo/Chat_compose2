@file:Suppress("DEPRECATION")

package com.dls.chatcompose2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.AuthRepository
import com.dls.chatcompose2.domain.use_case.auth.LoginUseCase
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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

    private val firestore = FirebaseFirestore.getInstance()

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

            val result = loginUseCase.signInWithGoogle(credential)

            _loginState.value = if (result.isSuccess) {
                // ✅ Recuperamos el usuario actual de FirebaseAuth
                FirebaseAuth.getInstance().currentUser?.let { firebaseUser ->
                    saveUserIfNew(firebaseUser)
                }
                Log.d("LoginViewModel", "Login con Google exitoso")
                LoginState(success = true)
            } else {
                Log.e("LoginViewModel", "Login con Google fallido: ${result.exceptionOrNull()?.message}")
                LoginState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Guarda el usuario en Firestore si no existe previamente.
     * Esto crea la colección `users` y añade un documento con UID como ID.
     */
    private fun saveUserIfNew(user: FirebaseUser) {

        val userDoc = firestore.collection("users").document(user.uid)

        userDoc.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    val newUser = User(
                        uid = user.uid,
                        name = user.displayName ?: "",
                        email = user.email ?: "",
                        photoUrl = user.photoUrl?.toString()
                    )

                    userDoc.set(newUser)
                        .addOnSuccessListener {
                            Log.d("LoginViewModel", "✅ Usuario guardado correctamente en Firestore")
                        }
                        .addOnFailureListener { e ->
                            Log.e("LoginViewModel", "❌ Error al guardar usuario en Firestore: ${e.localizedMessage}")
                        }
                } else {
                    Log.d("LoginViewModel", "ℹ️ Usuario ya existe en Firestore")
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "❌ Error al verificar existencia del usuario: ${e.localizedMessage}")
            }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            Log.d("LoginViewModel", "Iniciando login con email: $email")

            val result = loginUseCase(email, password)

            if (result.isSuccess) {
                Log.d("LoginViewModel", "✅ Login con email exitoso")
                _loginState.value = LoginState(success = true)
            } else {
                Log.e("LoginViewModel", "❌ Error en login: ${result.exceptionOrNull()?.localizedMessage}")
                _loginState.value = LoginState(error = result.exceptionOrNull()?.message)
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

