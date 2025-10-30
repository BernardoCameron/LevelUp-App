package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val dao = AppDatabase.instance.productDao()
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = dao.getAll()
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            dao.insert(product)
            loadProducts()
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            dao.deleteById(id)
            loadProducts()
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            dao.update(product)
            loadProducts()
        }
    }
}
