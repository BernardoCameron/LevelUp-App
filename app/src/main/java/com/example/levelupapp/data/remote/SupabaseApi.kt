package com.example.levelupapp.data.remote

import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Product
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Response





data class ProductPayload(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("foto")
    val foto: String,
    val categoriaId: Int,
    val destacado: Boolean,
    val codigoBarra: String?
)
interface SupabaseApi {



    @GET("producto")
    suspend fun getProductos(
        @Query("select") select: String = "*"
    ): List<Product>

    @GET("categoria")
    suspend fun getCategorias(
        @Query("select") select: String = "*"
    ): List<Categoria>

    @Headers("Prefer: return=representation")
    @POST("producto")
    suspend fun insertProducto(
        @Body producto: ProductPayload
    ): List<Product>

    // 👇 Editar producto (PATCH)
    @Headers("Prefer: return=representation")
    @PATCH("producto")
    suspend fun updateProducto(
        @Query("id") idFilter: String,
        @Body producto: ProductPayload
    ): List<Product>

    // 👇 Eliminar producto (DELETE)
    @DELETE("producto")
    suspend fun deleteProducto(
        @Query("id") idFilter: String    // ej: "eq.12"
    ): Response<Unit>

}
