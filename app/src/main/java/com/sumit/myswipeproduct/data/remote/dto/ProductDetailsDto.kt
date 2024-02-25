package com.sumit.myswipeproduct.data.remote.dto

data class ProductDetailsDto(
    val image: String="",
    val price: Double?,
    val product_name: String?,
    val product_type: String?,
    val tax: Double?
)
