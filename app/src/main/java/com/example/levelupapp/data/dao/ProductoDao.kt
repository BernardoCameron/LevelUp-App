package com.example.levelupapp.data.dao

import androidx.room.*
import com.example.levelupapp.data.model.Product

@Dao
interface ProductoDao {

    @Query("SELECT * FROM producto ORDER BY id ASC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<Product>>

    @Query("SELECT * FROM producto WHERE destacado = 1")
    fun getDestacados(): kotlinx.coroutines.flow.Flow<List<Product>>

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("DELETE FROM producto WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM producto")
    suspend fun countAll(): Int

    @Query("SELECT * FROM producto WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Product?

}
