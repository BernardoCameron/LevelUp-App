package com.example.levelupapp.data.repository

import com.example.levelupapp.data.dao.CredentialDao
import com.example.levelupapp.data.model.Credential

class AuthRepository(private val dao: CredentialDao) {

    suspend fun login(username: String, password: String): Boolean {
        val user = dao.getUserByUsername(username)
        return user?.password == password
    }

    suspend fun register(username: String, password: String): Boolean {
        if (dao.getUserByUsername(username) != null) return false
        dao.insert(Credential(username = username, password = password))
        return true
    }
}
