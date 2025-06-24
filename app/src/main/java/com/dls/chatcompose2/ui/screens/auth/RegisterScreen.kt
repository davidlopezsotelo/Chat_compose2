package com.dls.chatcompose2.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dls.chatcompose2.presentation.register.RegisterViewModel


@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    // Estados locales para los campos del formulario
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // Obtenemos el estado actual del ViewModel
    val uiState by registerViewModel.uiState.collectAsState()



    // Layout principal del formulario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de nombre
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de email
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Campo de contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro
        Button(
            onClick = {
                Log.d("RegisterScreen", "Botón de registro pulsado")
                if (name.value.isNotBlank() && email.value.isNotBlank() && password.value.length >= 6) {
                    registerViewModel.registerUser(name.value, email.value, password.value)
                } else {
                    Log.d("RegisterScreen", "Formulario incompleto o inválido")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Registrar")
        }

        // Indicador de carga
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Mensaje de error si ocurre algún fallo
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Navegación automática a Login tras éxito
        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) {
                Log.d("RegisterScreen", "Registro exitoso, navegando a Login")
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
                registerViewModel.resetState()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para volver a Login
        TextButton(
            onClick = {
                Log.d("RegisterScreen", "Volviendo a Login")
                navController.navigate("login")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
