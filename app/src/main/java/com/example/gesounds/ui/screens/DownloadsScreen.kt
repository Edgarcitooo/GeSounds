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
import androidx.navigation.NavController
import com.example.gesounds.viewmodel.SoundViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(navController: NavController, viewModel: SoundViewModel) {
    val context = LocalContext.current
    val descargas by viewModel.descargas.collectAsState()

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var idSonando by remember { mutableStateOf<Int?>(null) }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer?.release() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Descargas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 30.dp, bottom = 16.dp)
        )

        if (descargas.isEmpty()) {
            Text("Aún no tienes sonidos descargados.", color = Color.Gray)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(descargas) { sonido ->
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
                                    mediaPlayer = MediaPlayer.create(context, sonido.audioResId)
                                    mediaPlayer?.start()
                                    idSonando = sonido.id
                                }
                            } catch (e: Exception) {}
                        },
                        onToggleFavorito = { viewModel.toggleFavorito(sonido) },
                        onClickDownload = {
                           
                            viewModel.quitarDeDescargas(sonido)
                            Toast.makeText(context, "Eliminado de descargas", Toast.LENGTH_SHORT).show()
                        },
                        onClickShare = { compartirAudio(context, sonido) }
                    )
                }
            }
        }
    }
}