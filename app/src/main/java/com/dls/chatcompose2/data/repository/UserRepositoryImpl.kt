package com.dls.chatcompose2.data.repository

import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    override suspend fun getAllUsers(): Result<List<User>> = try {
        val currentUserId = auth.currentUser?.uid
        val snapshot = firestore.collection("users").get().await()

        val users = snapshot.documents.mapNotNull { doc ->
            val user = doc.toObject(User::class.java)
            if (user != null && user.uid != currentUserId) user else null
        }

        Result.success(users)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserById(uid: String): Result<User> = try {
        val doc = firestore.collection("users").document(uid).get().await()
        val user = doc.toObject(User::class.java)
        if (user != null) Result.success(user)
        else Result.failure(Exception("Usuario no encontrado"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
