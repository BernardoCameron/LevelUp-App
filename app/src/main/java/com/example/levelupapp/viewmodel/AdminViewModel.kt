package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        refreshProducts()
    }

    fun refreshProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProductos()
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    fun addProduct(product: Product) {
        _products.value = _products.value + product
    }

    fun updateProduct(product: Product) {
        _products.value = _products.value.map { if (it.id == product.id) product else it }
    }

    fun deleteProduct(id: Int) {
        _products.value = _products.value.filterNot { it.id == id }
    }
}
