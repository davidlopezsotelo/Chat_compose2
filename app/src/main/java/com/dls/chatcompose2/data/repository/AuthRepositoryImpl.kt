
package com.dls.chatcompose2.data.repository

import android.util.Log
import com.dls.chatcompose2.domain.model.User
import com.dls.chatcompose2.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("DEPRECATION")
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun loginWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "Login successful for $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun registerUser(name: String, email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid ?: throw Exception("User ID not found")
            val newUser = User(uid = userId, name = name, email = email)
            saveUserToFirestore(newUser)
            Log.d("AuthRepository", "User registered and saved: $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Result<Unit> {
        return try {
            auth.signInWithCredential(credential).await()
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    profilePictureUrl = firebaseUser.photoUrl?.toString()
                )
                saveUserToFirestore(user)
                Log.d("AuthRepository", "Google Sign-in successful for ${user.email}")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Google Sign-in failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        Log.d("AuthRepository", "Logging out")
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun saveUserToFirestore(user: User): Result<Unit> {
       return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Log.d("AuthRepository", "User saved to Firestore: ${user.uid}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error saving user to Firestore: ${e.message}")
           Result.failure(e)
        }
    }
}
