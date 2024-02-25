package com.sumit.myswipeproduct.data.remote.dto

data class ProductAddedResponseDto(
    val message: String?,
    val product_details: ProductDetailsDto?,
    val product_id: Int?,
    val success: Boolean?
)