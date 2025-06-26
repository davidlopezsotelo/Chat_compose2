package com.dls.chatcompose2.ui.screens.chats

import android.R.style
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dls.chatcompose2.presentation.chat.ChatViewModel
import com.dls.chatcompose2.ui.components.MainScaffold
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    navController: NavController,
    contactUid: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val contact by viewModel.contact.collectAsState()
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState() // Estado para la lista de mensajes


    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { viewModel.sendImage(it) }
        }
    )

    LaunchedEffect(contactUid) {
        viewModel.loadContact(contactUid)
    }

    MainScaffold(navController = navController, currentRoute = "chats") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            contact?.let { user ->
                Text(
                    text = "Chat con ${user.name}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }


            // 📨 Lista de mensajes
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    // muestra solo hora.
                    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
                    //val date = Date(msg.timestamp)
                    //val formattedTime = timeFormat.format(date)
                    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
                  //  val formattedDate = dateFormat.format(date)

                    val isMe = msg.senderId == FirebaseAuth.getInstance().currentUser?.uid


                  //  val isMe = msg.senderId == FirebaseAuth.getInstance().currentUser?.uid
                    val date = dateFormat.format(Date(msg.timestamp))
                    val time = timeFormat.format(Date(msg.timestamp))


//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 6.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        // Fecha centrada arriba del mensaje
//                        Text(
//                            text = date,
//                            style = MaterialTheme.typography.labelSmall.copy(
//                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
//                            )
//                        )
//
//                        // Contenedor del mensaje alineado izquierda/derecha
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .background(
//                                        if (isMe) MaterialTheme.colorScheme.primaryContainer
//                                        else MaterialTheme.colorScheme.surfaceVariant
//                                    )
//                                    .padding(12.dp)
//                                    .clip(MaterialTheme.shapes.medium)
//                                    .widthIn(max = 280.dp)
//                            ) {
//                                msg.imageUrl?.let { imageUrl ->
//                                    Image(
//                                        painter = rememberAsyncImagePainter(model = imageUrl),
//                                        contentDescription = "Imagen enviada",
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .clip(MaterialTheme.shapes.medium)
//                                    )
//                                    Spacer(modifier = Modifier.height(4.dp))
//                                }
//
//                                if (msg.text.isNotBlank()) {
//                                    Text(
//                                        text = msg.text,
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//                                }
//                            }
//                        }
//
//                        // Hora alineada a la derecha del mensaje
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 2.dp),
//                            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
//                        ) {
//                            Text(
//                                text = time,
//                                style = MaterialTheme.typography.labelSmall.copy(
//                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
//                                )
//                            )
//                        }
//                    }








                    //fecha

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dateFormat.format(Date(msg.timestamp)),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))



                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                    ) {        Column(
                        modifier = Modifier
                            .background(
                                if (isMe) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(12.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .widthIn(max = 280.dp) // 👈 muy importante para evitar que el mensaje crezca de más
                    ) {
                        msg.imageUrl?.let { imageUrl ->
                            Log.d("ChatScreen", "Mostrando imagen: $imageUrl")
                            Image(
                                painter = rememberAsyncImagePainter(model = imageUrl),
                                contentDescription = "Imagen enviada",
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(MaterialTheme.shapes.medium)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (msg.text.isNotBlank()) {
                            Text(text = msg.text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        //hora
                        Box(
                            modifier = Modifier
                               .fillMaxSize()
                                .padding(top = 4.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Text(
                                text = timeFormat.format(Date(msg.timestamp)),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }
                      }
                 }




                    LaunchedEffect(messages.size) {
                        if (messages.isNotEmpty()) {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
            }
        }

            Spacer(modifier = Modifier.height(8.dp))

            // ✍🏼 Campo de texto + botón enviar
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.sendMessage(messageText)
                    messageText = ""
                }) {
                    Text("Enviar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    imagePickerLauncher.launch("image/*")
                }) {
                    Text("📷")
                }
            }
        }
    }
}



