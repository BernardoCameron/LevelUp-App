package com.example.levelupapp.data.dao

import androidx.room.*
import com.example.levelupapp.data.model.Product

@Dao
interface ProductoDao {

    // get all products
    @Query("SELECT * FROM producto ORDER BY id DESC")
    suspend fun getAll(): List<Product>

    // get product by id
    @Query("SELECT * FROM producto WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Product?

    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    // update
    @Update
    suspend fun update(product: Product)

    // delete
    @Delete
    suspend fun delete(product: Product)

    // delete by id
    @Query("DELETE FROM producto WHERE id = :id")
    suspend fun deleteById(id: Int)
}
