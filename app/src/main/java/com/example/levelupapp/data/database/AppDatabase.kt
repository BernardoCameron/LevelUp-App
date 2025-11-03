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
    version = 16
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

                val categorias = listOf(
                    Categoria(nombre = "Redes"),
                    Categoria(nombre = "Audio"),
                    Categoria(nombre = "Computación"),
                    Categoria(nombre = "Periféricos"),
                    Categoria(nombre = "Telefonía"),
                    Categoria(nombre = "Consolas"),
                    Categoria(nombre = "Streaming / Creación"),
                    Categoria(nombre = "Mobiliario Gamer"),
                    Categoria(nombre = "Ropa y Merch"),
                    Categoria(nombre = "Almacenamiento / Servidores"),
                    Categoria(nombre = "Smart Home / LED"),
                    Categoria(nombre = "Ofertas / Accesorios Varios")
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


// 🔽 Productos adicionales por categoría (LevelUp ampliado)
                val newProducts = listOf(

                    // ======= REDES =======
                    Product(nombre = "Router Gaming TP-Link AX6000", descripcion = "Router WiFi 6 con doble banda y 8 antenas de alto rendimiento.", precio = 199990.0, imagen = "router_ax6000", categoriaId = 1),
                    Product(nombre = "Switch Gigabit 8 Puertos", descripcion = "Switch no gestionado de 8 puertos Gigabit Ethernet, ideal para LANs domésticas.", precio = 49990.0, imagen = "switch_8p", categoriaId = 1),
                    Product(nombre = "Repetidor WiFi Mesh", descripcion = "Extensor WiFi Mesh de alta cobertura, compatible con Alexa.", precio = 69990.0, imagen = "repetidor_mesh", categoriaId = 1),
                    Product(nombre = "Cable de Red Cat8 5m", descripcion = "Cable de red trenzado Cat8 de 5 metros, velocidad de hasta 40Gbps.", precio = 12990.0, imagen = "cable_cat8", categoriaId = 1),
                    Product(nombre = "Adaptador USB WiFi 6E", descripcion = "Adaptador WiFi 6E USB de doble banda, velocidad 2400Mbps.", precio = 39990.0, imagen = "adaptador_wifi6", categoriaId = 1),

                    // ======= AUDIO =======
                    Product(nombre = "Auriculares Logitech G733", descripcion = "Auriculares inalámbricos RGB con micrófono desmontable y sonido DTS:X.", precio = 119990.0, imagen = "logitech_g733", categoriaId = 2),
                    Product(nombre = "Micrófono Blue Yeti X", descripcion = "Micrófono USB profesional para streaming y grabaciones.", precio = 129990.0, imagen = "blue_yeti", categoriaId = 2),
                    Product(nombre = "Parlantes Razer Nommo Chroma", descripcion = "Sistema de parlantes 2.0 con efectos RGB y bajos potentes.", precio = 149990.0, imagen = "razer_nommo", categoriaId = 2),
                    Product(nombre = "Soundbar Gamer RGB", descripcion = "Barra de sonido con subwoofer integrado y conectividad Bluetooth.", precio = 99990.0, imagen = "soundbar_rgb", categoriaId = 2),
                    Product(nombre = "Audífonos Inalámbricos JBL Quantum TWS", descripcion = "Audífonos True Wireless diseñados para gaming con baja latencia.", precio = 79990.0, imagen = "jbl_tws", categoriaId = 2),

                    // ======= COMPUTACIÓN =======
                    Product(nombre = "Monitor LG UltraGear 27'' 165Hz", descripcion = "Monitor QHD de 27 pulgadas con FreeSync Premium y HDR10.", precio = 299990.0, imagen = "lg_ultragear", categoriaId = 3),
                    Product(nombre = "Notebook ASUS TUF Gaming", descripcion = "Notebook gamer Ryzen 7 con RTX 4060 y SSD 1TB.", precio = 1299990.0, imagen = "asus_tuf", categoriaId = 3),
                    Product(nombre = "PC Gamer MSI Aegis", descripcion = "PC gamer con Intel i7, RTX 4070 y 32GB RAM.", precio = 1499990.0, imagen = "msi_aegis", categoriaId = 3),
                    Product(nombre = "Disco SSD Kingston 1TB NVMe", descripcion = "SSD M.2 PCIe 4.0 con velocidades de lectura de 7000MB/s.", precio = 119990.0, imagen = "ssd_kingston", categoriaId = 3),
                    Product(nombre = "Memoria RAM Corsair 32GB DDR5", descripcion = "Kit dual DDR5 6000MHz RGB ideal para gaming extremo.", precio = 189990.0, imagen = "ram_corsair", categoriaId = 3),

                    // ======= PERIFÉRICOS =======
                    Product(nombre = "Mouse Logitech G Pro X Superlight", descripcion = "Mouse gamer inalámbrico ultraligero con sensor HERO 25K.", precio = 109990.0, imagen = "gpro_superlight", categoriaId = 4),
                    Product(nombre = "Teclado Mecánico Razer BlackWidow V4", descripcion = "Teclado con switches mecánicos verdes y RGB Chroma.", precio = 139990.0, imagen = "razer_blackwidow", categoriaId = 4),
                    Product(nombre = "Mousepad Corsair MM700 RGB", descripcion = "Alfombrilla extendida con iluminación RGB y hub USB integrado.", precio = 49990.0, imagen = "corsair_mm700", categoriaId = 4),
                    Product(nombre = "Controlador Elite Series 2", descripcion = "Control premium con gatillos ajustables y grip de goma.", precio = 189990.0, imagen = "elite_controller", categoriaId = 4),
                    Product(nombre = "Webcam Logitech StreamCam", descripcion = "Cámara Full HD 1080p60 con autoenfoque y soporte para streamers.", precio = 99990.0, imagen = "streamcam", categoriaId = 4),

                    // ======= TELEFONÍA =======
                    Product(nombre = "Smartphone iPhone 15 Pro", descripcion = "Pantalla OLED Super Retina XDR y chip A17 Pro.", precio = 1349990.0, imagen = "iphone_15pro", categoriaId = 5),
                    Product(nombre = "Smartphone Xiaomi 14", descripcion = "Pantalla AMOLED 120Hz, Snapdragon 8 Gen 3, 512GB.", precio = 899990.0, imagen = "xiaomi_14", categoriaId = 5),
                    Product(nombre = "Cargador Inalámbrico 3 en 1", descripcion = "Carga simultánea para smartphone, reloj y audífonos.", precio = 29990.0, imagen = "cargador_3en1", categoriaId = 5),
                    Product(nombre = "Auriculares Bluetooth Samsung Buds 3", descripcion = "Audífonos TWS con cancelación activa de ruido.", precio = 79990.0, imagen = "buds3", categoriaId = 5),
                    Product(nombre = "Powerbank 20000mAh", descripcion = "Cargador portátil con salida rápida USB-C y dos puertos USB-A.", precio = 39990.0, imagen = "powerbank", categoriaId = 5),

                    // ======= CONSOLAS =======
                    Product(nombre = "Xbox Series X", descripcion = "Consola 1TB SSD, 4K 120fps y mando inalámbrico incluido.", precio = 549990.0, imagen = "xbox_seriesx", categoriaId = 6),
                    Product(nombre = "Nintendo Switch OLED", descripcion = "Versión OLED con pantalla de 7 pulgadas y dock mejorado.", precio = 449990.0, imagen = "switch_oled", categoriaId = 6),
                    Product(nombre = "PlayStation 5 Slim", descripcion = "Versión compacta con lector de disco y 1TB SSD.", precio = 579990.0, imagen = "ps5_slim", categoriaId = 6),
                    Product(nombre = "Mando DualSense Edge", descripcion = "Control premium personalizable con gatillos ajustables.", precio = 199990.0, imagen = "dualsense_edge", categoriaId = 6),
                    Product(nombre = "Soporte Vertical PS5 + Cargador", descripcion = "Base con ventilación y carga dual para mandos.", precio = 39990.0, imagen = "base_ps5", categoriaId = 6),

                    // ======= COMPUTADORES GAMERS =======
                    Product(nombre = "PC Gamer ASUS ROG Strix RTX 4070Ti", descripcion = "Equipo gamer con procesador Intel i9 y gráfica RTX 4070Ti.", precio = 1999990.0, imagen = "rog_4070ti", categoriaId = 7),
                    Product(nombre = "PC Gamer NZXT H5 Elite", descripcion = "PC con Ryzen 7, RTX 4080 y refrigeración líquida AIO.", precio = 2299990.0, imagen = "nzxt_h5", categoriaId = 7),
                    Product(nombre = "Mini PC Gaming Intel NUC 13", descripcion = "Mini PC con RTX 4060, 32GB RAM y SSD 1TB NVMe.", precio = 1599990.0, imagen = "intel_nuc", categoriaId = 7),
                    Product(nombre = "PC Streaming Edition", descripcion = "PC optimizado para streaming y edición de video con OBS Studio.", precio = 1799990.0, imagen = "pc_stream", categoriaId = 7),
                    Product(nombre = "PC Gamer Custom RGB", descripcion = "PC armado con paneles de vidrio templado e iluminación ARGB.", precio = 1499990.0, imagen = "pc_rgb", categoriaId = 7),

                    // ======= SILLAS GAMERS =======
                    Product(nombre = "Silla Gamer Secretlab Titan Evo", descripcion = "Silla ergonómica con materiales premium y soporte lumbar ajustable.", precio = 349990.0, imagen = "secretlab_evo", categoriaId = 8),
                    Product(nombre = "Silla Cougar Armor One", descripcion = "Silla gamer con diseño racing y estructura de acero.", precio = 229990.0, imagen = "cougar_armor", categoriaId = 8),
                    Product(nombre = "Silla Ergohuman Elite", descripcion = "Silla de malla con soporte cervical ajustable.", precio = 379990.0, imagen = "ergohuman", categoriaId = 8),
                    Product(nombre = "Silla Gamer Drift DR111", descripcion = "Silla reclinable con reposapiés y cojines incluidos.", precio = 199990.0, imagen = "drift_dr111", categoriaId = 8),
                    Product(nombre = "Silla Razer Iskur", descripcion = "Silla con soporte lumbar ergonómico y acabados en cuero sintético.", precio = 299990.0, imagen = "razer_iskur", categoriaId = 8),

                    // ======= MOUSE / MOUSEPAD =======
                    Product(nombre = "Mouse Razer Basilisk V3", descripcion = "Mouse óptico de 26K DPI con iluminación Chroma RGB.", precio = 74990.0, imagen = "basilisk_v3", categoriaId = 9),
                    Product(nombre = "Mouse Logitech G903 Lightspeed", descripcion = "Mouse inalámbrico ambidiestro con sensor HERO 25K.", precio = 119990.0, imagen = "g903", categoriaId = 9),
                    Product(nombre = "Mousepad SteelSeries QcK Prism", descripcion = "Alfombrilla RGB dual con base antideslizante.", precio = 59990.0, imagen = "qck_prism", categoriaId = 9),
                    Product(nombre = "Mouse Redragon M808 Storm", descripcion = "Mouse ultraligero con diseño honeycomb RGB.", precio = 39990.0, imagen = "m808_storm", categoriaId = 9),
                    Product(nombre = "Mousepad HyperX Fury S Pro XL", descripcion = "Superficie microtexturizada para máxima precisión.", precio = 29990.0, imagen = "fury_s", categoriaId = 9),

                    // ======= POLERAS / MERCH =======
                    Product(nombre = "Polera Gamer 'Level-Up' Blanca", descripcion = "Polera algodón premium con logo Level-Up estampado.", precio = 14990.0, imagen = "polera_blanca", categoriaId = 10),
                    Product(nombre = "Polera Gamer Negra RGB", descripcion = "Polera gamer edición limitada con diseño RGB.", precio = 15990.0, imagen = "polera_negra", categoriaId = 10),
                    Product(nombre = "Gorro Gamer Retro 8-Bit", descripcion = "Gorro acrílico con logo retro de 8 bits bordado.", precio = 9990.0, imagen = "gorro_8bit", categoriaId = 10),
                    Product(nombre = "Taza Gamer 'Insert Coffee'", descripcion = "Taza cerámica con diseño gamer pixelado.", precio = 7990.0, imagen = "taza_gamer", categoriaId = 10),
                    Product(nombre = "Polerón Gamer ROG Edition", descripcion = "Hoodie de algodón con bordado Republic of Gamers.", precio = 24990.0, imagen = "poleron_rog", categoriaId = 10)
                )

                newProducts.forEach { productDao.insert(it) }
                println("Productos extra insertados (${newProducts.size})")


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