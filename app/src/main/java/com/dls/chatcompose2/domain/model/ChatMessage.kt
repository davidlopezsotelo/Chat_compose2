package com.dls.chatcompose2.domain.model

data class ChatMessage(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()

)
