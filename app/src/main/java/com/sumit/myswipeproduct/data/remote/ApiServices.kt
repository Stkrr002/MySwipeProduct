package com.sumit.myswipeproduct.data.remote

import com.sumit.myswipeproduct.data.remote.dto.ProductAddedResponseDto
import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {
    @GET("/api/public/get")
    suspend fun getAllProducts(): Response<List<ProductDetailsDto>>

    @Multipart
    @POST("/api/public/add")
    suspend fun addProduct(
        @Part("product_name") productName: String?,
        @Part("product_type") productType: String?,
        @Part("tax") tax: Double?,
        @Part("price") price: Double?,
        @Part files: List<MultipartBody.Part>
    ): Response<ProductAddedResponseDto>
}