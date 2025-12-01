package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    // ===== Datos de usuario =====
    private val _userName = MutableStateFlow("")
    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()

    // ===== Datos desde supabase =====
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())

    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    val featuredProducts: StateFlow<List<Product>> =
        _allProducts
            .map { list -> list.filter { it.destacado } }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )

    val recommendedProducts: StateFlow<List<Product>> =
        combine(_allProducts, _selectedCategoryId) { productos, catId ->
            when (catId) {
                null -> productos.filter { !it.destacado }
                else -> productos.filter { it.categoriaId == catId }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    init {
        viewModelScope.launch {
            loadCategorias()
            loadProductos()
        }
    }

    fun refreshProductos() {
        viewModelScope.launch {
            loadProductos()
        }
    }

    private suspend fun loadCategorias() {
        try {
            _categorias.value = repository.getCategorias()
        } catch (e: Exception) {
            e.printStackTrace()
            _categorias.value = emptyList()
        }
    }

    private suspend fun loadProductos() {
        try {
            _allProducts.value = repository.getProductos()
        } catch (e: Exception) {
            e.printStackTrace()
            _allProducts.value = emptyList()
        }
    }

    fun reloadProductos() {
        viewModelScope.launch {
            loadProductos()
        }
    }

    // ===== funciones que usa LoginScreen / MainScreen =====

    fun setUser(name: String, email: String) {
        _userName.value = name
        _userEmail.value = email
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setCategoriaSeleccionada(id: Int?) {
        _selectedCategoryId.value = id
    }
    // ==== SOLO PARA TESTS ====
    internal fun setProductsForTest(list: List<Product>) {
        _allProducts.value = list
    }
}
