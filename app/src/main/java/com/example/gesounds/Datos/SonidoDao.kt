package com.example.gesounds.Datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SonidoDao {
    @Query("SELECT * FROM sonidos_table ORDER BY categoria ASC")
    fun getAllSonidos(): Flow<List<Sonido>>

    @Query("SELECT * FROM sonidos_table WHERE categoria = :categoria")
    fun getSonidosByCategoria(categoria: String): Flow<List<Sonido>>


    @Query("SELECT * FROM sonidos_table WHERE esFavorito = 1")
    fun getFavoritos(): Flow<List<Sonido>>

    @Update
    suspend fun updateSonido(sonido: Sonido)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSonido(sonido: Sonido)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sonidos: List<Sonido>)
}