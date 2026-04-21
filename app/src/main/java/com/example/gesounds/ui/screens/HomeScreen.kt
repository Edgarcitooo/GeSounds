package com.example.gesounds.ui.screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gesounds.Datos.Sonido
import com.example.gesounds.ui.navigation.Screen
import com.example.gesounds.viewmodel.SoundViewModel
import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: SoundViewModel) {
    val context = LocalContext.current
    val sonidos by viewModel.sonidos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "GeSoundboard",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Button(
                        onClick = { navController.navigate(Screen.Profile.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("\uD83D\uDC64 Perfil", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6750A4),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("⏬")
                    }

                    Button(
                        onClick = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("🏠 Soundboard")
                    }

                    Button(
                        onClick = { navController.navigate(Screen.Favorites.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("★ Favoritos")
                    }

                    Button(
                        onClick = { navController.navigate(Screen.Search.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("\uD83D\uDD0E Buscar")
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(sonidos) { sonido ->
                        SoundItemComposable(
                            sonido = sonido,
                            onClickPlay = {
                                try {
                                    val mediaPlayer = MediaPlayer.create(context, sonido.audioResId)
                                    mediaPlayer.start()
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al reproducir", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onToggleFavorito = {
                                viewModel.toggleFavorito(sonido)
                                val msg = if (sonido.esFavorito) "Quitado de favoritos" else "Añadido a favoritos"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                            onClickDownload = {

                                guardarAudioEnDescargas(context, sonido)
                            },
                            onClickShare = {

                                compartirAudio(context, sonido)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SoundItemComposable(
    sonido: Sonido,
    onClickPlay: () -> Unit,
    onToggleFavorito: () -> Unit,
    onClickDownload: () -> Unit,
    onClickShare: () -> Unit
) {
    val colorString = if (sonido.colorHex.startsWith("#")) sonido.colorHex else "#${sonido.colorHex}"
    val buttonColor = try {
        Color(android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        Color(0xFF333333)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClickPlay,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Text(
                text = sonido.nombre.replace(" ", "\n"),
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onToggleFavorito,
                modifier = Modifier.height(30.dp).weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sonido.esFavorito) Color(0xFFFFD700) else Color(0xFF4A397A)
                )
            ) {
                Text("★", fontSize = 14.sp, color = if (sonido.esFavorito) Color.Black else Color.White)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = onClickDownload,
                modifier = Modifier.height(30.dp).weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
            ) {
                Text("⏬", fontSize = 14.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = onClickShare,
                modifier = Modifier.height(30.dp).weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
            ) {
                Text("➦", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
fun guardarAudioEnDescargas(context: Context, sonido: Sonido) {
    try {
        val inputStream = context.resources.openRawResource(sonido.audioResId)
        val fileName = "${sonido.nombre.replace("\n", "_")}.mp3"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/GeSounds")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            val outputStream = resolver.openOutputStream(uri)
            if (outputStream != null) {
                inputStream.copyTo(outputStream)
                outputStream.close()
            }
            inputStream.close()
            Toast.makeText(context, "✅ Guardado en Descargas", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "❌ Error al crear el archivo", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "❌ Error al descargar", Toast.LENGTH_SHORT).show()
    }
}
fun compartirAudio(context: Context, sonido: com.example.gesounds.Datos.Sonido) {
    try {
        val file = File(context.cacheDir, "${sonido.nombre.replace("\n", "_")}.mp3")
        val inputStream = context.resources.openRawResource(sonido.audioResId)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/mpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir audio..."))

    } catch (e: Exception) {
        android.widget.Toast.makeText(context, "Error al compartir", android.widget.Toast.LENGTH_SHORT).show()
    }
}