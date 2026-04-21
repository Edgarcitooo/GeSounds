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
import androidx.compose.ui.res.stringResource
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
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val soundsList = listOf(
        "Grito\nHomero",
        "Gallo en\nel arbol",
        "Que\nRikooo",
        "Que\nque que",
        "Musica\nSad",
        "Ete\nSech",
        "Ba Dum\nTss",
        "Sonido\nGrillos",
        "Batería\nFail",
        "Aleluya",
        "Gato\nLlorando",
        "Wow\nAnime",

    )

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.Soundboard),
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
                    items(soundsList.size) { index ->
                        SoundItemComposable(
                            soundTitle = soundsList[index],
                            onClickPlay = {
                                Toast.makeText(context, "Sonando: ${soundsList[index].replace("\n", " ")}", Toast.LENGTH_SHORT).show()
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
    soundTitle: String,
    onClickPlay: () -> Unit
) {
    val buttonColor = Color(0xFF333333)

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
                text = soundTitle,
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
                onClick = { },
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text("★", fontSize = 10.sp)
            }

            Button(
                onClick = {  },
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text("⏬", fontSize = 10.sp)
            }

            Button(
                onClick = { },
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text("➦", fontSize = 10.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val mockNavController = rememberNavController()
    MaterialTheme {
        HomeScreen(mockNavController)
    }
}