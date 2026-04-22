package com.example.gesounds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gesounds.Datos.Usuario
import com.example.gesounds.Datos.UsuarioDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UsuarioDao) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            dao.insertUsuario(usuario)
            val nuevoUser = dao.login(usuario.correo, usuario.contrasena)
            if (nuevoUser != null) {
                _usuarioActual.value = nuevoUser
            }
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            dao.updateUsuario(usuario)
            _usuarioActual.value = usuario
        }
    }

    fun login(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = dao.login(correo, contrasena)
            if (user != null) {
                _usuarioActual.value = user
            }
            onResult(user != null)
        }
    }

    fun logout() {
        _usuarioActual.value = null
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