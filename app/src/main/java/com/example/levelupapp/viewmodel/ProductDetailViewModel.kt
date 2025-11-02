package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.database.AppDatabase
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    fun loadProductById(productId: Int) {
        viewModelScope.launch {
            try {
                val productDao = AppDatabase.instance.productDao()
                val result = productDao.getById(productId)
                _product.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
