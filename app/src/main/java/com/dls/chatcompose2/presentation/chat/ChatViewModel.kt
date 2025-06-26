package com.dls.chatcompose2.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.model.ChatMessage
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.MessageRepository
import com.dls.chatcompose2.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _contact = MutableStateFlow<User?>(null)
    val contact: StateFlow<User?> = _contact

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private var chatId: String? = null

    fun loadContact(uid: String) {
        viewModelScope.launch {
            val result = userRepository.getUserById(uid)
            result.onSuccess {
                _contact.value = it
                startObservingMessages(it.uid)
            }.onFailure {
                Log.e("ChatViewModel", "Error al cargar contacto", it)
            }
        }
    }

    private fun startObservingMessages(contactUid: String) {
        val senderUid = auth.currentUser?.uid ?: return
        chatId = listOf(senderUid, contactUid).sorted().joinToString("_")

        viewModelScope.launch {
            messageRepository.observeMessages(chatId!!).collect { list ->
                _messages.value = list
            }
        }
    }

    fun sendMessage(text: String) {
        val receiver = contact.value ?: return
        val senderId = auth.currentUser?.uid ?: return

        val message = ChatMessage(
            senderId = senderId,
            receiverId = receiver.uid,
            text = text.trim(),
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            messageRepository.sendMessage(message)
        }
    }
}

