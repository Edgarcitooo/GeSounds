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
    val audioResId: Int,
    val esFavorito: Boolean = false
)