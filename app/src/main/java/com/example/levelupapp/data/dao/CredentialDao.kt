package com.example.levelupapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupapp.data.model.Credential

@Dao
interface CredentialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(credential: Credential)

    @Query("SELECT * FROM credential_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): Credential?
}
