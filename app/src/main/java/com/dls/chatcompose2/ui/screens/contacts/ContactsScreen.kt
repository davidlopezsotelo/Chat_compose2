package com.dls.chatcompose2.ui.screens.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dls.chatcompose2.R
import com.dls.chatcompose2.presentation.home.UserViewModel
import com.dls.chatcompose2.ui.components.MainScaffold

@Composable
fun ContactsScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val contacts by viewModel.contactList.collectAsState()

    // Al iniciar la pantalla, cargar los usuarios
    LaunchedEffect(Unit) {
        viewModel.fetchContacts()
    }

    MainScaffold(navController = navController, currentRoute = "contacts") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Contactos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(contacts) { user ->
                    ContactItem(user = user) {
                        navController.navigate("chat/${user.uid}")
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(user: com.dls.chatcompose2.domain.model.User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = user.photoUrl ?: "",
                placeholder = painterResource(id = R.drawable.default_user),
                error = painterResource(id = R.drawable.default_user)
            ),
            contentDescription = "Foto de ${user.name}",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = user.name ?: "Sin nombre",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
