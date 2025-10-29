package com.example.levelupapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producto")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val nombre: String,
    val precio: String,
    val imagen: String
)