@file:Suppress("DEPRECATION")

package com.dls.chatcompose2.data.firebase

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.dls.chatcompose2.data.firebase.Commons.GOOGLE_WEB_CLIENT_ID
import com.dls.chatcompose2.domain.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Cliente personalizado para autenticación con Google usando One Tap Sign-In API
 * Gestiona el intento de login y la recuperación del usuario autenticado.
 */
class GoogleSignInClient (
    private val activity: Activity
) {

    private val auth = Firebase.auth
    private val oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(activity)
    }

    private val signInRequest: BeginSignInRequest by lazy {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Web Client ID obtenido desde Firebase Console > Authentication > Google
                    .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    /**
     * Lanza la petición de login con Google y devuelve el intent para iniciar el flujo de login
     */
    suspend fun getSignInIntent(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            Log.d("GoogleSignInClient", "Intent de login obtenido con éxito")
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            Log.e("GoogleSignInClient", "Error obteniendo login intent: ${e.localizedMessage}")
            null
        }
    }

    /**
     * Procesa el resultado del login y autentica al usuario en Firebase
     */
    suspend fun signInWithGoogle(credential: SignInCredential): Result<User> {
        return try {
            val idToken = credential.googleIdToken
                ?: return Result.failure(Exception("No se recibió ID Token"))

            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(firebaseCredential).await()

            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("No se pudo obtener el usuario de Firebase"))

            val user = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString()
            )

            Log.d("GoogleSignInClient", "Login con Google exitoso: ${user.email}")
            Result.success(user)
        } catch (e: Exception) {
            Log.e("GoogleSignInClient", "Error en login con Google: ${e.localizedMessage}")
            Result.failure(e)
        }
    }
}
