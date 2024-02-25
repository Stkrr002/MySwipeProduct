package com.sumit.myswipeproduct.domain.repository

import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.responsehandler.APIResponse

interface HomeScreenRepository {
    suspend fun getAllProducts(fromServer:Boolean): APIResponse<List<ProductItem?>?>

    suspend fun addProduct(productItem: ProductItem):APIResponse<String>
}