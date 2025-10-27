package com.example.levelupapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.levelupapp.data.dao.CredentialDao
import com.example.levelupapp.data.model.Credential

@Database(entities = [Credential::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao
}