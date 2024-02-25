package com.sumit.myswipeproduct.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sumit.myswipeproduct.data.local.ProductItemEntity

@Dao
interface ProductItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productDetail: List<ProductItemEntity?>)

    @Query("SELECT * FROM product_details")
    suspend fun getAllProductItems(): List<ProductItemEntity?>


    @Query("DELETE FROM product_details")
    suspend fun clearProductDetails()
}
