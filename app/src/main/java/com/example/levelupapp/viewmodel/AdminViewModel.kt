package com.example.levelupapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.remote.SupabaseStorageClient
import com.example.levelupapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri

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
            val remote = repository.getProductos()
            _products.value = remote
        }
    }


    fun addProductWithImage(
        context: Context,
        imageUri: Uri,
        nombre: String,
        descripcion: String,
        precio: Double,
        categoriaId: Int,
        destacado: Boolean,
        codigoBarras: String?
    ) {
        viewModelScope.launch {
            val resolver = context.contentResolver
            val bytes = resolver.openInputStream(imageUri)?.use { it.readBytes() }

            if (bytes == null) {
                // si falla se agrega sin img
                addProduct(
                    Product(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio,
                        imagen = "default_image",
                        categoriaId = categoriaId,
                        destacado = destacado,
                        codigoBarras = codigoBarras
                    )
                )
                return@launch
            }

            val mimeType = resolver.getType(imageUri) ?: "image/jpeg"
            val extension = when {
                mimeType.endsWith("png") -> "png"
                mimeType.endsWith("webp") -> "webp"
                else -> "jpg"
            }

            val fileName = "product_${System.currentTimeMillis()}.$extension"


            val imageUrl = SupabaseStorageClient.uploadImage(
                bytes = bytes,
                mimeType = mimeType,
                fileName = fileName
            )

            val finalImage = imageUrl ?: "default_image"


            val created = repository.addProduct(
                Product(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precio,
                    imagen = finalImage,
                    categoriaId = categoriaId,
                    destacado = destacado,
                    codigoBarras = codigoBarras
                )
            )

            if (created != null) {
                _products.value = _products.value + created
                refreshProducts()
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            val created = repository.addProduct(product)
            if (created != null) {
                _products.value = _products.value + created
                refreshProducts()
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            val updated = repository.updateProduct(product)
            if (updated != null) {
                _products.value = _products.value.map {
                    if (it.id == updated.id) updated else it
                }
                refreshProducts()
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            val ok = repository.deleteProduct(id)
            if (ok) {
                _products.value = _products.value.filterNot { it.id == id }
                refreshProducts()
            }
        }
    }
}
