package com.example.levelupapp.data.repository

import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.data.remote.SupabaseClient
import com.example.levelupapp.data.remote.SupabaseApi

class ProductRepository(
    private val api: SupabaseApi = SupabaseClient.api
) {

    suspend fun getProductos(): List<Product> {
        return api.getProductos()
    }

    suspend fun getCategorias(): List<Categoria> {
        return api.getCategorias()
    }
}
