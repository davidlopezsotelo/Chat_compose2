package com.dls.chatcompose2.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _contact = MutableStateFlow<User?>(null)
    val contact: StateFlow<User?> = _contact

    fun loadContact(uid: String) {
        viewModelScope.launch {
            val result = userRepository.getUserById(uid)
            result.onSuccess {
                _contact.value = it
            }.onFailure {
                // Puedes loguear o mostrar un error con Snackbar
                 Log.e("ChatViewModel", "Error al cargar el contacto", it)
            }
        }
    }
}
