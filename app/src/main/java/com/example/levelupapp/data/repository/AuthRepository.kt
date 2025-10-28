package com.example.levelupapp.data.repository

import com.example.levelupapp.data.dao.CredentialDao
import com.example.levelupapp.data.model.Credential

class AuthRepository(private val dao: CredentialDao) {

    suspend fun login(email: String, password: String): Credential? {
        val user = dao.getUserByEmail(email)
        return if (user?.password == password) user else null
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        if (dao.getUserByEmail(email) != null) return false

        val dctoDuoc = email.endsWith("@duocuc.cl")
        dao.insert(
            Credential(
                username = name,
                email = email,
                password = password,
                dctoDuoc = dctoDuoc
            )
        )
        return true
    }
}
