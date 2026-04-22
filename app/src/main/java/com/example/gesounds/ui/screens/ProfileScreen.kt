package com.example.gesounds.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gesounds.ui.navigation.Screen
import com.example.gesounds.viewmodel.UserViewModel
import androidx.core.content.FileProvider
import java.io.File


fun crearUriParaCamara(context: Context): Uri {
    val file = File(context.cacheDir, "foto_perfil_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val usuarioActual by userViewModel.usuarioActual.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let { user ->
            nombres = user.nombres
            apellidos = user.apellidos
            username = user.nombreUsuario
            email = user.correo
            telefono = user.telefono
            fechaNacimiento = user.fechaNacimiento
            password = user.contrasena
            if (user.fotoUri != null) {
                imageUri = Uri.parse(user.fotoUri)
            }
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {

            imageUri = tempCameraUri
        }
    }

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
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Salir", color = Color.White)
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            usuarioActual?.let { user ->
                                val userActualizado = user.copy(
                                    nombres = nombres,
                                    apellidos = apellidos,
                                    nombreUsuario = username,
                                    correo = email,
                                    telefono = telefono,
                                    fechaNacimiento = fechaNacimiento,
                                    contrasena = password,

                                    fotoUri = imageUri?.toString() ?: user.fotoUri
                                )
                                userViewModel.actualizarUsuario(userActualizado)
                                Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
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

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sin foto",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                ) {
                    Text("📁 Galería", color = Color.White)
                }
                Button(
                    onClick = {
                        try {

                            val uri = crearUriParaCamara(context)
                            tempCameraUri = uri
                            cameraLauncher.launch(uri)
                        } catch (e: Exception) {

                            Toast.makeText(context, "Error de cámara: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A))
                ) {
                    Text("📷 Cámara", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditableProfileItemRow(label = "Nombre(s)", value = nombres, onValueChange = { nombres = it })
            EditableProfileItemRow(label = "Apellidos", value = apellidos, onValueChange = { apellidos = it })
            EditableProfileItemRow(label = "Nombre de usuario", value = username, onValueChange = { username = it })
            EditableProfileItemRow(label = "Correo electrónico", value = email, onValueChange = { email = it })
            EditableProfileItemRow(label = "Número de teléfono", value = telefono, onValueChange = { telefono = it })
            EditableProfileItemRow(label = "Fecha de nacimiento", value = fechaNacimiento, onValueChange = { fechaNacimiento = it })
            EditableProfileItemRow(label = "Contraseña", value = password, onValueChange = { password = it })

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A397A)),
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
fun EditableProfileItemRow(label: String, value: String, onValueChange: (String) -> Unit) {
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

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "✏️",
                    fontSize = 14.sp
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray
        )
    }
}