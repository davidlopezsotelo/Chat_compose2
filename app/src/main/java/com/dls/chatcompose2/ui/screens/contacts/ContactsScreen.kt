package com.dls.chatcompose2.ui.screens.contacts

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
 * Pantalla base para mostrar la lista de contactos.
 * Aquí se podrán implementar futuras funciones como agregar amigos, buscar contactos, etc.
 */
@Composable
fun ContactsScreen(navController: NavController) {
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
                text = "Pantalla de Contactos",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
