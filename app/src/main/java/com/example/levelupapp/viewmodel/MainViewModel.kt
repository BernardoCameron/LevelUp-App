package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val productoDao = AppDatabase.instance.productDao()
    private val catDao = AppDatabase.instance.categoriaDao()
    private val _userName = MutableStateFlow("")
    private val _userEmail = MutableStateFlow("")
    val userEmail = _userEmail.asStateFlow()
    val userName = _userName.asStateFlow()

    val featuredProducts: StateFlow<List<Product>> =
        productoDao.getDestacados().
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recommendedProducts: StateFlow<List<Product>> =
        productoDao.getAll()
            .map { it.filter { prod -> !prod.destacado } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categorias: StateFlow<List<Categoria>> =
        AppDatabase.instance.categoriaDao().getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            productoDao.getAll().collect { list ->
                println("🧩 Productos en DB: ${list.size}")
            }
        }
    }

    fun setUser(name: String, email: String) {
        _userName.value = name
        _userEmail.value = email
    }

    fun setUserName(name: String) {
        _userName.value = name
    }
}
