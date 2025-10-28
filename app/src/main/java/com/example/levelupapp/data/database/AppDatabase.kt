package com.example.levelupapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.levelupapp.data.dao.CredentialDao
import com.example.levelupapp.data.model.Credential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Credential::class], version = 2) // 🔹 incrementa la versión
abstract class AppDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "levelup_dbv2"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Inserta usuario inicial en la BD
            CoroutineScope(Dispatchers.IO).launch {
                getDatabase(context).credentialDao().insert(
                    Credential(
                        username = "Admin",
                        email = "admin@levelup.com",
                        password = "1234",
                        dctoDuoc = false
                    )
                )
            }
        }
    }
}
