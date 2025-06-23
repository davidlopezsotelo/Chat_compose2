package com.dls.chatcompose2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import android.util.Log
import androidx.compose.material3.Text

/**
 * Este composable será el punto de entrada para la navegación de la app.
 * Aquí se llamará al NavGraph una vez esté implementado.
 */
@Composable
fun AppEntryPoint() {
    // Solo como punto de partida, luego se usará NavHost aquí
    LaunchedEffect(Unit) {
        Log.d("AppEntryPoint", "Interfaz iniciada correctamente")
    }

    Greeting("Chat Compose 2") // Placeholder mientras se crea NavGraph
}
@Composable
fun Greeting(name: String) {
    Text(text = "Hola $name!") // Implementación de ejemplo
    Log.d("Greeting", "Greeting composable called with name: $name")
}


