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
    entities = [Credential::class, Product::class, Categoria::class],
    version = 11
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun credentialDao(): CredentialDao
    abstract fun productDao(): ProductoDao

    abstract fun categoriaDao(): CategoriaDao

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
                val productDao = database.productDao()
                val count = productDao.countAll()
                if (count == 0) {
                    println("BD abierta pero vacía, insertando datos base.........")
                    insertInitialData(context)
                } else {
                    println("BD abierta con $count productos existentes")
                }
            }
        }

        private fun insertInitialData(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                val categoriaDao = database.categoriaDao()
                val productDao = database.productDao()
                val credentialDao = database.credentialDao()

                // insert categorias base
                val categorias = listOf(
                    Categoria(nombre = "Redes"),
                    Categoria(nombre = "Audio"),
                    Categoria(nombre = "Computación"),
                    Categoria(nombre = "Periféricos"),
                    Categoria(nombre = "Telefonía")
                )
                categorias.forEach { categoriaDao.insert(it) }
                println("Categorías insertadas (${categorias.size})")

                // insert de productos base
                val initialProducts = listOf(
                    Product(nombre = "Monitor 24''", descripcion = "Monitor LED Full HD 24 pulgadas", precio = 119990.0, imagen = "monitor", categoriaId = 3, destacado = false),
                    Product(nombre = "Notebook HP", descripcion = "Notebook HP Ryzen 5 8GB RAM SSD 512GB", precio = 499990.0, imagen = "notebook", categoriaId = 3, destacado = true),
                    Product(nombre = "Auriculares JBL", descripcion = "Auriculares Bluetooth JBL Tune 510BT", precio = 29990.0, imagen = "headphones", categoriaId = 2, destacado = true),
                    Product(nombre = "Teclado Mecánico RGB", descripcion = "Teclado mecánico retroiluminado con switches azules", precio = 45990.0, imagen = "keyboard", categoriaId = 4, destacado = true),
                    Product(nombre = "Mouse Inalámbrico Logitech", descripcion = "Mouse ergonómico inalámbrico", precio = 15990.0, imagen = "mouse", categoriaId = 4, destacado = true),
                    Product(nombre = "Parlante JBL GO 3", descripcion = "Parlante portátil JBL resistente al agua", precio = 89990.0, imagen = "microfono", categoriaId = 2),
                    Product(nombre = "Smartphone Samsung A54", descripcion = "Teléfono inteligente Samsung A54 128GB", precio = 279990.0, imagen = "phone", categoriaId = 5, destacado = true),
                    Product(nombre = "Silla Gamer Razer", descripcion = "Silla ergonómica con soporte lumbar", precio = 229990.0, imagen = "chair", categoriaId = 4),
                    Product(nombre = "Tablet Lenovo", descripcion = "Tablet Lenovo M10 con pantalla 10.1''", precio = 229990.0, imagen = "tablet", categoriaId = 3),
                    Product(nombre = "Router TP-Link AX3000", descripcion = "Router WiFi 6 doble banda", precio = 99990.0, imagen = "router", categoriaId = 1, destacado = true)
                )

                initialProducts.forEach { productDao.insert(it) }
                println("Productos base insertados (${initialProducts.size})")

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