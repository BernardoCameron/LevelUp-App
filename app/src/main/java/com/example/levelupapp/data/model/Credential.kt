package com.example.levelupapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credential_table")
data class Credential(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val dctoDuoc: Boolean = false
)