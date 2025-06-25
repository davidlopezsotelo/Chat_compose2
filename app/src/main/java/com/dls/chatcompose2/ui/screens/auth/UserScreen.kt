package com.dls.chatcompose2.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.dls.chatcompose2.R
import com.dls.chatcompose2.presentation.home.UserViewModel

@Composable
fun UserScreen(
    navController: NavHostController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        userState?.let { user ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Imagen de perfil
                Image(
                    painter = rememberAsyncImagePainter(
                        model = user.photoUrl ?: "",
                        placeholder = painterResource(id = R.drawable.default_user),
                        error = painterResource(id = R.drawable.default_user)
                    ),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                Text(
                    text = user.name ?: "Nombre no disponible",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email
                Text(
                    text = user.email ?: "Correo no disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Datos adicionales
                user.phone?.takeIf { it.isNotBlank() }?.let { phone ->
                    Text(
                        text = "📞 Teléfono: $phone",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                user.address?.takeIf { it.isNotBlank() }?.let { address ->
                    Text(
                        text = "🏠 Dirección: $address",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                user.occupation?.takeIf { it.isNotBlank() }?.let { occupation ->
                    Text(
                        text = "💼 Ocupación: $occupation",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Editar perfil
                Button(
                    onClick = {
                        navController.navigate("edit_user")
                    }
                ) {
                    Text("Editar perfil")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Cerrar sesión
                OutlinedButton(
                    onClick = {
                        viewModel.logout()
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    val mockUserState = com.dls.chatcompose2.domain.model.User(
        uid = "mockUid",
        name = "David López",
        email = "david@email.com",
        phone = "654321987",
        address = "Calle Falsa 123, Bilbao",
        occupation = "Developer",
        photoUrl = ""
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.default_user),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mockUserState.name,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mockUserState.email,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "📞 Teléfono: ${mockUserState.phone}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🏠 Dirección: ${mockUserState.address}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "💼 Ocupación: ${mockUserState.occupation}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {}) {
                Text("Editar perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = {}) {
                Text("Cerrar sesión")
            }
        }
    }
}
