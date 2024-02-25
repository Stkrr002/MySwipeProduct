package com.sumit.myswipeproduct.domain.repository

import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.responsehandler.APIResponse
import java.io.File

interface HomeScreenRepository {
    suspend fun getAllProducts(fromServer:Boolean): APIResponse<List<ProductItem?>?>

    suspend fun addProduct(productItem: ProductItem, productImage: File?):APIResponse<ProductItem?>
}