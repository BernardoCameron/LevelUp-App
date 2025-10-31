package com.example.levelupapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)
