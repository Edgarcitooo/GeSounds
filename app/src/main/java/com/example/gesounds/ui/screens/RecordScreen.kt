package com.example.gesounds.ui.screens


import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gesounds.Datos.Sonido
import com.example.gesounds.viewmodel.SoundViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(navController: NavController, viewModel: SoundViewModel) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var nombreSonido by remember { mutableStateOf("") }
    var rutaAudioGrabado by remember { mutableStateOf<String?>(null) }
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }

    // Pedir permiso en tiempo real
    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Se necesita el micrófono para grabar", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grabar Sonido", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("⬅ Volver", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Button(
                onClick = {
                    if (!hasPermission) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        if (isRecording) {

                            try {
                                mediaRecorder?.stop()
                                mediaRecorder?.release()
                                mediaRecorder = null
                                isRecording = false
                                Toast.makeText(context, "Grabación terminada", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error al detener", Toast.LENGTH_SHORT).show()
                            }
                        } else {

                            try {
                                val archivo = File(context.filesDir, "grabacion_${System.currentTimeMillis()}.mp4")
                                rutaAudioGrabado = archivo.absolutePath

                                mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    MediaRecorder(context)
                                } else {
                                    MediaRecorder()
                                }.apply {
                                    setAudioSource(MediaRecorder.AudioSource.MIC)
                                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                    setOutputFile(archivo.absolutePath)
                                    prepare()
                                    start()
                                }
                                isRecording = true
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error al grabar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.size(150.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) Color.Red else Color(0xFF4A397A)
                )
            ) {
                Text(
                    text = if (isRecording) "⏹ DETENER" else "🎙 GRABAR",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (rutaAudioGrabado != null && !isRecording) {
                OutlinedTextField(
                    value = nombreSonido,
                    onValueChange = { nombreSonido = it },
                    label = { Text("Nombre de tu sonido", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF6750A4),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (nombreSonido.isNotBlank()) {

                            val nuevoSonido = Sonido(
                                nombre = nombreSonido,
                                categoria = "Mis Grabaciones",
                                colorHex = "#FF0000",
                                audioResId = 0,
                                rutaLocal = rutaAudioGrabado
                            )
                            viewModel.agregarSonidoGrabado(nuevoSonido)
                            Toast.makeText(context, "¡Sonido guardado en tu Soundboard!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Ponle un nombre primero", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
                ) {
                    Text("Guardar en el Soundboard", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}