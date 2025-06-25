package com.dls.chatcompose2.presentation.home

import android.net.Uri

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Result.Companion.failure
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUser()?.uid
            if (userId != null) {
                val result = authRepository.getUserFromFirestore(userId)
                result.fold(
                    onSuccess = { user ->
                        _userState.value = user
                        Log.d("UserViewModel", "✅ Usuario obtenido: ${user.email}")
                    },
                    onFailure = { e ->
                        Log.e("UserViewModel", "❌ Error al obtener usuario: ${e.localizedMessage}")
                    }
                )
            } else {
                Log.w("UserViewModel", "⚠️ No hay usuario autenticado.")
            }
        }
    }
    suspend fun updateUser(user: User, newPhotoUri: Uri? = null): Result<Unit> {
        return try {
            val updatedUser = if (newPhotoUri != null) {
                val photoUrlResult = authRepository.uploadProfilePicture(user.uid, newPhotoUri)
                val photoUrl = photoUrlResult.getOrElse {
                    Log.e("UserViewModel", "❌ Error al subir foto: ${it.localizedMessage}")
                    return failure(it)
                }
                user.copy(photoUrl = photoUrl)
            } else {
                user
            }

            val result = authRepository.saveUserToFirestore(updatedUser)
            result.fold(
                onSuccess = {
                    _userState.value = updatedUser
                    Log.d("UserViewModel", "✅ Usuario actualizado correctamente")
                },
                onFailure = {
                    Log.e("UserViewModel", "❌ Error al guardar usuario: ${it.localizedMessage}")
                }
            )
            result
        } catch (e: Exception) {
            Log.e("UserViewModel", "❌ Error inesperado: ${e.localizedMessage}")
            failure(e)
        }
    }
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutSuccess.value = true
            Log.d("UserViewModel", "👋 Sesión cerrada correctamente.")
        }
    }
}