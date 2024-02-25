package com.sumit.myswipeproduct.data.remote

import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {
    @GET("/api/public/get")
    suspend fun getAllProducts(): Response<List<ProductDetailsDto>>
}