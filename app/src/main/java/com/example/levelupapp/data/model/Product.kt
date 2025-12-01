package com.example.levelupapp.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("foto")
    val imagen: String,        // viene de la columna "foto" en Supabase
    val categoriaId: Int,
    val destacado: Boolean = false,
    @SerializedName("codigoBarra")   // nombre de la columna en supabase, para no cambiar en el code
    val codigoBarras: String? = null
)