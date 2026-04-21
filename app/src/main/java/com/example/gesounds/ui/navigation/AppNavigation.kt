package com.example.gesounds.ui.navigation

import LoginScreen
import ProfileScreen
import RegisterScreen
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
import androidx.compose.remote.creation.profile.Profile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gesounds.ui.screens.FavoritesScreen
import com.example.gesounds.ui.screens.HomeScreen
import com.example.gesounds.ui.screens.SearchScreen



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
    ) { innerPading ->
        Box(modifier = Modifier.padding(innerPading)) {
            NavHost(
                navController = navController, startDestination = Screen.Login.route
            ) {
                composable(route = Screen.Login.route) {
                    LoginScreen(navController)

                }


                composable(route = Screen.Register.route) {
                    RegisterScreen(navController)
                }

                composable(route = Screen.Home.route) {
                    HomeScreen(navController)
                }
                composable(route = Screen.Profile.route) {
                    ProfileScreen(navController)}

                composable(route = Screen.Search.route) {
                    SearchScreen(navController)
                }

                composable(route = Screen.Favorites.route) {
                    FavoritesScreen(navController)


                }

            }

        }
    }
}
