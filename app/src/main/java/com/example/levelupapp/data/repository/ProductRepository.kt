package com.example.levelupapp.data.repository

import android.util.Log
import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.remote.ProductPayload
import com.example.levelupapp.data.remote.SupabaseApi
import com.example.levelupapp.data.remote.SupabaseClient
import retrofit2.Response

class ProductRepository(
    private val api: SupabaseApi = SupabaseClient.api
) {

    suspend fun getProductos(): List<Product> {
        return try {
            val productos = api.getProductos()
            Log.d("SupabaseRepo", "getProductos -> ${productos.size} items")
            productos
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Error al obtener productos", e)
            emptyList()
        }
    }

    suspend fun getCategorias(): List<Categoria> {
        return try {
            val cats = api.getCategorias()
            Log.d("SupabaseRepo", "getCategorias -> ${cats.size} items")
            cats
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Error al obtener categorias", e)
            emptyList()
        }
    }

    // Crear producto
    suspend fun addProduct(product: Product): Product? {
        return try {
            val payload = ProductPayload(
                nombre = product.nombre,
                descripcion = product.descripcion,
                precio = product.precio,
                foto = product.imagen,
                categoriaId = product.categoriaId,
                destacado = product.destacado,
                codigoBarra = product.codigoBarras
            )

            val created = api.insertProducto(payload)
            created.firstOrNull()
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Error al agregar producto", e)
            null
        }
    }

    // Editar producto
    suspend fun updateProduct(product: Product): Product? {
        return try {
            val payload = ProductPayload(
                nombre = product.nombre,
                descripcion = product.descripcion,
                precio = product.precio,
                foto = product.imagen,
                categoriaId = product.categoriaId,
                destacado = product.destacado,
                codigoBarra = product.codigoBarras
            )

            val updated = api.updateProducto(
                idFilter = "eq.${product.id}",
                producto = payload
            )
            updated.firstOrNull()
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Error al actualizar producto", e)
            null
        }
    }

    // 🔽 Eliminar producto
    suspend fun deleteProduct(id: Int): Boolean {
        return try {
            val response = api.deleteProducto(idFilter = "eq.$id")
            if (response.isSuccessful) {
                true
            } else {
                Log.e("SupabaseRepo", "Error al eliminar producto: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Error al eliminar producto", e)
            false
        }
    }
}
