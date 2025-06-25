package com.dls.chatcompose2.ui.screens.chats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dls.chatcompose2.ui.components.MainScaffold

/**
 * Pantalla base para mostrar la lista de chats activos o recientes.
 * Aquí se mostrará la vista de conversaciones.
 */
@Composable
fun ChatsScreen(navController: NavController) {
    MainScaffold(navController= navController, currentRoute = "contacts") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pantalla de Chats",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
