package com.sumit.myswipeproduct.data.remote

import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @GET("/api/public/get")
    suspend fun getAllProducts(): Response<List<ProductDetailsDto>>

    @FormUrlEncoded
    @POST("/api/public/add")
    suspend fun addProduct(
        @Field("product_name") product_name: String?,
        @Field("product_type") product_type: String?,
        @Field("tax") tax: Double?,
        @Field("price") price: Double?,
        @Field("image") image: String?
    ): Response<String>
}