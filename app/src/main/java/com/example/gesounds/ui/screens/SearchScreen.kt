package com.example.gesounds.ui.screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gesounds.viewmodel.SoundViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SoundViewModel) {
    val context = LocalContext.current
    val todosLosSonidos by viewModel.sonidos.collectAsState()

    var textoBusqueda by remember { mutableStateOf("") }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var idSonando by remember { mutableStateOf<Int?>(null) }

    val sonidosFiltrados = remember(textoBusqueda, todosLosSonidos) {
        if (textoBusqueda.isEmpty()) {
            todosLosSonidos
        } else {
            todosLosSonidos.filter {
                it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                        it.categoria.contains(textoBusqueda, ignoreCase = true)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer?.release() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        placeholder = { Text("Buscar sonidos...", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("⬅", color = Color.White, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (sonidosFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron resultados", color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(sonidosFiltrados) { sonido ->
                        SoundItemComposable(
                            sonido = sonido,
                            onClickPlay = {
                                try {
                                    if (idSonando == sonido.id && mediaPlayer?.isPlaying == true) {
                                        mediaPlayer?.stop()
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        idSonando = null
                                    } else {
                                        mediaPlayer?.stop()
                                        mediaPlayer?.release()

                                        if (sonido.rutaLocal != null) {
                                            mediaPlayer = MediaPlayer().apply {
                                                setDataSource(sonido.rutaLocal)
                                                prepare()
                                            }
                                        } else {
                                            mediaPlayer = MediaPlayer.create(context, sonido.audioResId)
                                        }

                                        mediaPlayer?.start()
                                        idSonando = sonido.id

                                        mediaPlayer?.setOnCompletionListener {
                                            it.release()
                                            mediaPlayer = null
                                            idSonando = null
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al reproducir", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onToggleFavorito = { viewModel.toggleFavorito(sonido) },
                            onClickDownload = {
                                guardarAudioEnDescargas(context, sonido)
                                viewModel.marcarComoDescargado(sonido)
                            },
                            onClickShare = { compartirAudio(context, sonido) }
                        )
                    }
                }
            }
        }
    }
}