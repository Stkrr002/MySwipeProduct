package com.sumit.myswipeproduct.data.repository

import android.content.Context
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.data.local.ProductItemDao
import com.sumit.myswipeproduct.data.mapper.toProductEntityListGeneric
import com.sumit.myswipeproduct.data.mapper.toProductItem
import com.sumit.myswipeproduct.data.mapper.toProductItemListGeneric
import com.sumit.myswipeproduct.data.remote.ApiServices
import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.domain.repository.HomeScreenRepository
import com.sumit.myswipeproduct.responsehandler.APIResponse
import com.sumit.myswipeproduct.responsehandler.ResponseHandler
import com.sumit.myswipeproduct.responsehandler.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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

    override suspend fun addProduct(
        productItem: ProductItem,
        productImage: File?
    ): APIResponse<ProductItem?> {
        val productImageRes = getProductImageMultipart(productImage)
        val result = responseHandler.callAPI {
            apiServices.addProduct(
                productItem.product_name,
                productItem.product_type,
                productItem.tax,
                productItem.price,
                productImageRes
            )
        }

        return result.map {
            it.toProductItem()
        }
    }

    private fun getProductImageMultipart(productImage: File?): List<MultipartBody.Part> {
        val productImageRes = mutableListOf<MultipartBody.Part>()
        if (productImage != null) {
            val requestFile = productImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body =
                MultipartBody.Part.createFormData("image", productImage.name, requestFile)
            productImageRes.add(body)
        }
        return productImageRes
    }


}


