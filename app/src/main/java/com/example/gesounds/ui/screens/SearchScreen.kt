package com.example.gesounds.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gesounds.R

@Composable
fun SearchScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = stringResource(id = R.string.search_title), style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { navController.popBackStack() }) { Text("Volver al inicio") }
    }
}
@Preview(showBackground = true)
@Composable
private fun preview(){
    val navControllerLocal = rememberNavController()
    SearchScreen(navControllerLocal)
}