package com.dls.chatcompose2.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dls.chatcompose2.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Log.d("HomeScreen", "Volviendo al Login")
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
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
