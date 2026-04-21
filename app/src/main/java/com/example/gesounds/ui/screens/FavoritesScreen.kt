package com.example.gesounds.ui.screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.gesounds.ui.navigation.Screen
import com.example.gesounds.viewmodel.SoundViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, viewModel: SoundViewModel) {
    val context = LocalContext.current
    val favoritos by viewModel.favoritos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favoritos",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (favoritos.isEmpty()) Arrangement.Center else Arrangement.Top
            ) {
                if (favoritos.isEmpty()) {
                    Text(
                        text = "Aún no tienes sonidos favoritos.\n¡Ve al Soundboard y agrega algunos!",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(favoritos) { sonido ->
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
                                    Toast.makeText(context, "Quitado de favoritos", Toast.LENGTH_SHORT).show()
                                },
                                onClickDownload = {
                                    Toast.makeText(context, "Descarga en desarrollo...", Toast.LENGTH_SHORT).show()
                                },
                                onClickShare = {
                                    Toast.makeText(context, "Compartir en desarrollo...", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}