package com.example.levelupapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.levelupapp.data.model.Credential

@Dao
interface CredentialDao {
    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(credential: Credential)
    // select username by id
    @Query("SELECT * FROM credential_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): Credential?
    //get username by email
    @Query("SELECT * FROM credential_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): Credential?
}
