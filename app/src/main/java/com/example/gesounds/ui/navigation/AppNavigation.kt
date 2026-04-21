package com.example.gesounds.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gesounds.Datos.AppDatabase
import com.example.gesounds.ui.screens.FavoritesScreen
import com.example.gesounds.ui.screens.HomeScreen
import com.example.gesounds.ui.screens.LoginScreen
import com.example.gesounds.ui.screens.ProfileScreen
import com.example.gesounds.ui.screens.RegisterScreen
import com.example.gesounds.ui.screens.SearchScreen
import com.example.gesounds.viewmodel.SoundViewModel
import com.example.gesounds.viewmodel.SoundViewModelFactory
import com.example.gesounds.viewmodel.UserViewModel
import com.example.gesounds.viewmodel.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GeSounds") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            val context = LocalContext.current
            val database = AppDatabase.getInstance(context)

            val soundFactory = SoundViewModelFactory(database.sonidoDao())
            val soundViewModel: SoundViewModel = viewModel(factory = soundFactory)

            val userFactory = UserViewModelFactory(database.usuarioDao())
            val userViewModel: UserViewModel = viewModel(factory = userFactory)

            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                composable(route = Screen.Login.route) {
                    LoginScreen(navController = navController, userViewModel = userViewModel)
                }

                composable(route = Screen.Register.route) {
                    RegisterScreen(navController = navController, userViewModel = userViewModel)
                }

                composable(route = Screen.Home.route) {
                    HomeScreen(navController = navController, viewModel = soundViewModel)
                }

                composable(route = Screen.Profile.route) {
                    ProfileScreen(navController = navController, userViewModel = userViewModel)
                }

                composable(route = Screen.Search.route) {
                    SearchScreen(navController)
                }

                composable(route = Screen.Favorites.route) {
                    FavoritesScreen(navController = navController, viewModel = soundViewModel)


                }
            }
        }
    }
}