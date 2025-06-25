package com.dls.chatcompose2.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _chatContacts = MutableStateFlow<List<User>>(emptyList())
    val chatContacts: StateFlow<List<User>> = _chatContacts

    fun loadChatContacts() {
        val currentUid = auth.currentUser?.uid ?: return
        val ref = db.getReference("chats").child(currentUid)

        ref.get().addOnSuccessListener { snapshot ->
            val contactUids = snapshot.children.mapNotNull { it.key }

            viewModelScope.launch {
                val results = contactUids.mapNotNull { uid ->
                    userRepository.getUserById(uid).getOrNull()
                }
                _chatContacts.value = results
            }
        }
    }
}
