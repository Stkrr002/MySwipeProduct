package com.sumit.myswipeproduct.data.repository

import android.content.Context
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.data.local.ProductItemDao
import com.sumit.myswipeproduct.data.mapper.toProductEntityListGeneric
import com.sumit.myswipeproduct.data.mapper.toProductItemListGeneric
import com.sumit.myswipeproduct.data.remote.ApiServices
import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.domain.repository.HomeScreenRepository
import com.sumit.myswipeproduct.responsehandler.APIResponse
import com.sumit.myswipeproduct.responsehandler.ResponseHandler
import com.sumit.myswipeproduct.responsehandler.map
import javax.inject.Inject

class HomeScreenRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val responseHandler: ResponseHandler,
    private val productDao: ProductItemDao?,
    private val context: Context
) : HomeScreenRepository {
    override suspend fun getAllProducts(
        fromServer: Boolean
    ): APIResponse<List<ProductItem?>?> {

        val result: APIResponse<List<ProductDetailsDto>>

        if (!fromServer) {
            val productEntityList = productDao?.getAllProductItems()

            if (!productEntityList.isNullOrEmpty()) {
                return APIResponse.Success(productEntityList.toProductItemListGeneric())
            }

        } else {
            result = responseHandler.callAPI(
                call = { apiServices.getAllProducts() }
            ).also {
                if (it is APIResponse.Success) {
                    val productEntityList = it.data.toProductEntityListGeneric()
                    productDao?.insert(productEntityList)
                }
            }
            return result.map {
                it.toProductItemListGeneric()
            }
        }
        return APIResponse.Error(context.getString(R.string.no_data_found))
    }

}


