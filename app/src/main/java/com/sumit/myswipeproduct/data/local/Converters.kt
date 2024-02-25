package com.sumit.myswipeproduct.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto


class Converters {
    @TypeConverter
    fun fromProductList(value: List<ProductDetailsDto>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toProductList(value: String?): List<ProductDetailsDto>? {
        val listType = object : TypeToken<List<ProductDetailsDto>?>() {}.type
        return Gson().fromJson(value, listType)
    }

}