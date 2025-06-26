package com.dls.chatcompose2.domain.repository

import com.dls.chatcompose2.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(message: ChatMessage): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<ChatMessage>>
}
