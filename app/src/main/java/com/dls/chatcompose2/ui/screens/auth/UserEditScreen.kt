package com.dls.chatcompose2.ui.screens.auth

import android.net.Uri
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dls.chatcompose2.R
import com.dls.chatcompose2.presentation.home.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onUserUpdated: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var phone by remember { mutableStateOf(userState?.phone ?: "") }
    var address by remember { mutableStateOf(userState?.address ?: "") }
    var occupation by remember { mutableStateOf(userState?.occupation ?: "") }
    var name by remember { mutableStateOf(userState?.name ?: "") }
    var email by remember { mutableStateOf(userState?.email ?: "") }

    Column(Modifier.padding(16.dp)) {
        Text("Editar perfil", style = MaterialTheme.typography.titleLarge)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri.value),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_user),
                    contentDescription = "Default Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, enabled = false)
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") })
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") })
        OutlinedTextField(value = occupation, onValueChange = { occupation = it }, label = { Text("Ocupación") })

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }

            Button(onClick = {
                userState?.let {
                    val updatedUser = it.copy(
                        name = name,
                        phone = userState?.phone ?: "",
                        address = userState?.address ?: "",
                        occupation = userState?.occupation ?: ""
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val result : Result<Unit> = viewModel.updateUser(updatedUser, imageUri.value)
                            if (result.isSuccess) {
                                Toast.makeText(context, "✅ Perfil actualizado", Toast.LENGTH_SHORT).show()
                                onUserUpdated()
                            } else {
                                Toast.makeText(context, "❌ Error: ${result.exceptionOrNull()?.localizedMessage ?: "Error desconocido"}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "❌ Excepción: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }) {
                Text("Guardar")
            }
        }
    }
}