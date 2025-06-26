package com.dls.chatcompose2.data.repository

import android.net.Uri
import com.dls.chatcompose2.domain.model.ChatMessage
import com.dls.chatcompose2.domain.repository.MessageRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase
) : MessageRepository {

    private fun getChatId(senderId: String, receiverId: String): String {
        return listOf(senderId, receiverId).sorted().joinToString("_")
    }



    override suspend fun sendMessage(message: ChatMessage): Result<Unit> = try {
        val chatId = getChatId(message.senderId, message.receiverId)
        val ref = db.getReference("messages").child(chatId).push()
        ref.setValue(message).await()

        // Registrar chat activo para ambos usuarios
        val chatRef = db.getReference("chats")
        chatRef.child(message.senderId).child(message.receiverId).setValue(true)
        chatRef.child(message.receiverId).child(message.senderId).setValue(true)

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun uploadImage(
        uri: Uri,
        chatId: String,
    ): Result<String> = try {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = FirebaseStorage.getInstance().reference
            .child("chat_images")
            .child(chatId)
            .child(fileName)
        ref.putFile(uri).await()
        val imageUrl = ref.downloadUrl.await().toString()
        Result.success(imageUrl)
    }catch (e: Exception) {
        Result.failure(e)
    }


    override fun observeMessages(chatId: String): Flow<List<ChatMessage>> = callbackFlow {
        val ref = db.getReference("messages").child(chatId)

        val listener = ref.orderByChild("timestamp").addValueEventListener(
            object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val messages = snapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
                    trySend(messages)
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
            }
        )

        awaitClose { ref.removeEventListener(listener) }
    }
}
