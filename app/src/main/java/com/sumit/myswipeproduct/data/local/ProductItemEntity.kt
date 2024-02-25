package com.sumit.myswipeproduct.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_details")
data class ProductItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "product_name") val product_name: String?,
    @ColumnInfo(name = "product_type") val product_type: String?,
    @ColumnInfo(name = "tax") val tax: Double?
)

