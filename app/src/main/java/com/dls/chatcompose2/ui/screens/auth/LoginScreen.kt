@file:Suppress("DEPRECATION")

package com.dls.chatcompose2.ui.screens.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dls.chatcompose2.data.firebase.Commons.GOOGLE_WEB_CLIENT_ID
import com.dls.chatcompose2.presentation.login.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signInClient = remember { Identity.getSignInClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = Identity.getSignInClient(context)
                        .getSignInCredentialFromIntent(result.data)
                    viewModel.onGoogleSignInResult(credential)
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Error al obtener credencial de Google: ${e.localizedMessage}")
                }
            }
        }
    )


    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = Identity.getSignInClient(context)
                    .getSignInCredentialFromIntent(result.data)
                viewModel.onGoogleSignInResult(credential)
            } catch (e: ApiException) {
                Log.e("LoginScreen", "Google sign-in failed", e)
            }
        }
    }

    LaunchedEffect(loginState.success) {
        if (loginState.success) {
            Log.d("LoginScreen", "Login exitoso, navegando a Home")
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login con Email")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Lanzar el intent de Google Sign-In
            val request = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(GOOGLE_WEB_CLIENT_ID) // Reemplaza con tu Client ID de Firebase
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .setAutoSelectEnabled(true)
                .build()

            signInClient.beginSignIn(request)
                .addOnSuccessListener { result ->
                    try {
                        launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Error al lanzar Google Sign-In: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener {
                    Log.e("LoginScreen", "Fallo al iniciar sesión con Google: ${it.localizedMessage}")
                }

        }
        ) {
            Text("Login con Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        if (loginState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        loginState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}



