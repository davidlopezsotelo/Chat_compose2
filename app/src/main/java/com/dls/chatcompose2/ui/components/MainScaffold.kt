package com.dls.chatcompose2.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.dls.chatcompose2.R


@Composable
fun MainScaffold(
    navController: NavController,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val items = listOf("user", "contacts", "chats")
    val selectedIndex = items.indexOf(currentRoute).coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Usuario") },
                    selected = selectedIndex == 0,
                    onClick = { navController.navigate("user") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_contacts), contentDescription = "Contactos") },
                    label = { Text("Contactos") },
                    selected = selectedIndex == 1,
                    onClick = { navController.navigate("contacts") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = "Chats") },
                    label = { Text("Chats") },
                    selected = selectedIndex == 2,
                    onClick = { navController.navigate("chats") }
                )
            }
        },
        content = content
    )
}


