package com.dls.chatcompose2.ui.screens.chats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dls.chatcompose2.presentation.chat.ChatViewModel
import com.dls.chatcompose2.ui.components.MainScaffold

@Composable
fun ChatScreen(
    navController: NavController,
    contactUid: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val contact by viewModel.contact.collectAsState()

    // Carga el contacto al iniciar
    LaunchedEffect(contactUid) {
        viewModel.loadContact(contactUid)
    }

    MainScaffold(navController = navController, currentRoute = "chats") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            contact?.let { user ->
                Text(
                    text = "Chat con ${user.name ?: "Desconocido"}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Email: ${user.email}")
                // Aquí irá el chat en tiempo real
            } ?: Text("Cargando contacto...")
        }
    }
}


