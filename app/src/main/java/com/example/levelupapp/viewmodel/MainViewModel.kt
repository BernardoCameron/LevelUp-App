package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.levelupapp.data.model.Product

class MainViewModel : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _featuredProducts = MutableStateFlow<List<Product>>(emptyList())
    val featuredProducts = _featuredProducts.asStateFlow()

    private val _recommendedProducts = MutableStateFlow<List<Product>>(emptyList())
    val recommendedProducts = _recommendedProducts.asStateFlow()

    init {
        //cambiar a datos de la bd
        _featuredProducts.value = listOf(
            Product(1, "Auriculares", "$29.990", "headphones"),
            Product(2, "Teclado Mecánico", "$45.990", "keyboard"),
            Product(3, "Mouse Inalámbrico", "$15.990", "mouse")
        )

        _recommendedProducts.value = listOf(
            Product(4, "Monitor 24''", "$119.990", "monitor"),
            Product(5, "Tablet Lenovo", "$229.990", "tablet"),
            Product(6, "Notebook HP", "$499.990", "notebook"),
            Product(7, "Auriculares", "$29.990", "headphones"),
            Product(8, "Parlante JBL", "$89.990", "microfono")
        )
    }

    fun setUserName(name: String) {
        _userName.value = name
    }
}
