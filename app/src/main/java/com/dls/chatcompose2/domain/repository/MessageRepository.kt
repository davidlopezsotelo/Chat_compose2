package com.dls.chatcompose2.domain.repository

import android.net.Uri
import com.dls.chatcompose2.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(message: ChatMessage): Result<Unit>

    suspend fun uploadImage(uri: Uri, chatId: String): Result<String>


    fun observeMessages(chatId: String): Flow<List<ChatMessage>>
}
