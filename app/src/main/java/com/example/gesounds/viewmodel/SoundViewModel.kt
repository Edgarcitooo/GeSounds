package com.example.gesounds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gesounds.Datos.Sonido
import com.example.gesounds.Datos.SonidoDao
import com.example.gesounds.R
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SoundViewModel(private val dao: SonidoDao) : ViewModel() {

    val sonidos: StateFlow<List<Sonido>> = dao.getAllSonidos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun obtenerSonidosFiltrados(usuarioActualId: Int?): List<Sonido> {
        return sonidos.value.filter { sonido ->
            sonido.usuarioId == null || sonido.usuarioId == usuarioActualId
        }
    }

    val favoritos: StateFlow<List<Sonido>> = dao.getFavoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val descargas: StateFlow<List<Sonido>> = dao.getDescargados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            dao.getAllSonidos().collect { lista ->
                if (lista.isEmpty()) {
                    cargarSonidosIniciales()
                }
            }
        }
    }

    private suspend fun cargarSonidosIniciales() {
        val iniciales = listOf(
            Sonido(nombre = "clap clap", categoria = "Memes", colorHex = "#FF4B2B", audioResId = R.raw.aplausos),
            Sonido(nombre = "fahhh", categoria = "Memes", colorHex = "#3B82F6", audioResId = R.raw.fahh),
            Sonido(nombre = "miaaau", categoria = "Gatos", colorHex = "#10B981", audioResId = R.raw.miau)
        )
        dao.insertAll(iniciales)
    }

    fun toggleFavorito(sonido: Sonido) {
        viewModelScope.launch {
            val actualizado = sonido.copy(esFavorito = !sonido.esFavorito)
            dao.updateSonido(actualizado)
        }
    }

    fun marcarComoDescargado(sonido: Sonido) {
        viewModelScope.launch {
            dao.updateSonido(sonido.copy(esDescargado = true))
        }
    }

    fun quitarDeDescargas(sonido: Sonido) {
        viewModelScope.launch {
            dao.updateSonido(sonido.copy(esDescargado = false))
        }
    }

    fun agregarSonidoGrabado(sonido: Sonido) {
        viewModelScope.launch {
            dao.insertSonido(sonido)
        }
    }
}

class SoundViewModelFactory(private val dao: SonidoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoundViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}