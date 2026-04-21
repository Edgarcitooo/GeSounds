package com.example.gesounds.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gesounds.R
import com.example.gesounds.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current

    val soundsList = listOf(
        "Ba Dum\nTss",
        "Sonido\nGrillos",
        "Batería\nFail",
        "Aleluya",

    )

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
                        onClick = { navController.navigate(Screen.Home.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("⏬ Descargas")
                    }


                    Button(
                        onClick = { navController.navigate(Screen.Home.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                    ) {
                        Text("🏠 Inicio")
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
                    items(soundsList.size) { index ->
                        SoundItemComposable(
                            soundTitle = soundsList[index],
                            onClickPlay = {
                                Toast.makeText(context, "Toñando: ${soundsList[index].replace("\n", " ")}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    val mockNavController = rememberNavController()
    MaterialTheme {
        FavoritesScreen(mockNavController)
    }
}