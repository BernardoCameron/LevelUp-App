package com.example.levelupapp.data.remote

import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface SupabaseApi {

    @GET("producto")
    suspend fun getProductos(
        @Query("select") select: String = "*"
    ): List<Product>

    @GET("categoria")
    suspend fun getCategorias(
        @Query("select") select: String = "*"
    ): List<Categoria>
}
