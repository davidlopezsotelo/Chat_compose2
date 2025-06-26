package com.dls.chatcompose2.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dls.chatcompose2.R

@Composable
fun HomeScreen(navController: NavController) {
    // Estado local para resaltar el botón seleccionado
    var selectedItem by remember { mutableIntStateOf(0) }

    listOf("user", "contacts", "chats")

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Botón de usuario
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
                    label = { Text("Usuario") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("user")
                    }
                )
                // Botón de contactos
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Contactos") },
                    label = { Text("Contactos") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("contacts")
                    }
                )
                // Botón de chats
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = "Chats") },
                    label = { Text("Chats") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("chats")
                    }
                )
            }
        }
    ) { padding ->
        // Contenido principal del HomeScreen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pantalla Principal", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                Log.d("HomeScreen", "Cerrar sesión")
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }) {
                Text("Cerrar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                Log.d("HomeScreen", "Editar perfil")
                navController.navigate("edit_user")
            }) {
                Text("Editar perfil")
            }
        }
    }
}

