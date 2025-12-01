package com.example.levelupapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.levelupapp.data.dao.CategoriaDao
import com.example.levelupapp.data.dao.CredentialDao
import com.example.levelupapp.data.dao.ProductoDao
import com.example.levelupapp.data.model.Categoria
import com.example.levelupapp.data.model.Credential
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Credential::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun credentialDao(): CredentialDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        lateinit var instance: AppDatabase
            private set

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "levelup_db"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = dbInstance
                instance = dbInstance
                dbInstance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            insertInitialData(context)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
            }
        }

        private fun insertInitialData(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                val credentialDao = database.credentialDao()



                // insert usuario admin
                credentialDao.insert(
                    Credential(
                        username = "Admin",
                        email = "admin@levelup.com",
                        password = "1234",
                        dctoDuoc = false
                    )
                )
                println("Usuario admin insertado")
            }
        }
    }



}