package com.dls.chatcompose2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.dls.chatcompose2.ui.navigation.NavGraph
import com.dls.chatcompose2.ui.theme.ChatCompose2Theme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity es el punto de entrada de la aplicación.
 * Se encarga de aplicar el tema y cargar la navegación principal (NavGraph).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activamos modo edge-to-edge en dispositivos modernos
        enableEdgeToEdge()

        Log.d("MainActivity", "Aplicación iniciada - onCreate")

        // Cargamos el contenido Compose
        setContent {
            ChatCompose2Theme {
                // Surface actúa como contenedor del fondo con el color del tema
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Aquí se iniciará el flujo de navegación de la app
                    AppEntryPoint()
                }
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
