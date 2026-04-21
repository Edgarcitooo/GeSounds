package com.example.gesounds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gesounds.Datos.Usuario
import com.example.gesounds.Datos.UsuarioDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UsuarioDao) : ViewModel() {

    val usuarioActual: StateFlow<Usuario?> = dao.getUsuarioActual()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            dao.insertUsuario(usuario)
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            dao.updateUsuario(usuario)
        }
    }

    fun login(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = dao.login(correo, contrasena)
            onResult(user != null)
        }
    }
}

class UserViewModelFactory(private val dao: UsuarioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}