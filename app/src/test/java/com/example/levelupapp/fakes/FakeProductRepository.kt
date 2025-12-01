package com.example.levelupapp.fakes

import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.repository.ProductRepository

class FakeProductRepository(
    initialProducts: List<Product> = emptyList(),
    private val categorias: List<Categoria> = emptyList()
) : ProductRepository() {

    private val productos = initialProducts.toMutableList()

    override suspend fun getProductos(): List<Product> = productos.toList()

    override suspend fun getCategorias(): List<Categoria> = categorias

    override suspend fun addProduct(product: Product): Product? {
        val newId = if (productos.isEmpty()) 1 else productos.maxOf { it.id } + 1
        val withId = if (product.id == 0) product.copy(id = newId) else product
        productos.add(withId)
        return withId
    }

    override suspend fun updateProduct(product: Product): Product? {
        val idx = productos.indexOfFirst { it.id == product.id }
        if (idx == -1) return null
        productos[idx] = product
        return product
    }

    override suspend fun deleteProduct(id: Int): Boolean {
        return productos.removeIf { it.id == id }
    }
}
