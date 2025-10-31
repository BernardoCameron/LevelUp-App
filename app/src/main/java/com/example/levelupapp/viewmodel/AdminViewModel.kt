package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class AdminViewModel : ViewModel() {

    private val productoDao = AppDatabase.instance.productDao()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        observeProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productoDao.getAll().collectLatest { list ->
                _products.value = list
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            productoDao.insert(product)
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            productoDao.deleteById(id)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productoDao.update(product)
        }
    }
}
