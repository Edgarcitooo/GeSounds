import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = "Editar perfil",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Salir", color = Color.White)
                    }
                },
                actions = {
                    Button(onClick = {  }) {
                        Text("Guardar", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color.DarkGray
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.padding(24.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { }) {
                Text("Cambiar foto de perfil", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileItemRow(label = "Nombre(s)", value = "Edgar Adrian")
            ProfileItemRow(label = "Apellidos", value = "Garcia Perez")
            ProfileItemRow(label = "Nombre de usuario", value = "Eddgarcitoo")


            ProfileItemRow(label = "Correo electrónico", value = "12345678@gmail.com")
            ProfileItemRow(label = "Número de teléfono", value = "99-32-00-21-33")
            ProfileItemRow(label = "Fecha de nacimiento", value = "26/08/2006")
            ProfileItemRow(label = "Contraseña", value = "********")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileItemRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val navControllerLocal = rememberNavController()
    MaterialTheme {
        ProfileScreen(navControllerLocal)
    }
}