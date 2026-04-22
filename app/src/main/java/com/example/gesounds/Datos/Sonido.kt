package com.example.gesounds.Datos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sonidos_table")
data class Sonido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val categoria: String,
    val colorHex: String,
    val audioResId: Int = 0,
    val esFavorito: Boolean = false,
    val esDescargado: Boolean = false,
    val rutaLocal: String? = null,
    val usuarioId: Int? = null
)