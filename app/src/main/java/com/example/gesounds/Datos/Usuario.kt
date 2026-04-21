package com.example.gesounds.Datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios_table")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombres: String,
    val apellidos: String,
    val nombreUsuario: String,
    val correo: String,
    val telefono: String,
    val fechaNacimiento: String,
    val contrasena: String,
    val fotoUri: String? = null
)