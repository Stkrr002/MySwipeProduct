package com.sumit.myswipeproduct.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.domain.repository.HomeScreenRepository
import com.sumit.myswipeproduct.responsehandler.APIResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository
) : ViewModel() {


    private val _productDataList =
        MutableLiveData<APIResponse<List<ProductItem?>?>>()
    val productDataList: LiveData<APIResponse<List<ProductItem?>?>>
        get() = _productDataList


    fun getProductDataList(fromServer: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {
            _productDataList.postValue(APIResponse.Loading())

            val response = homeScreenRepository.getAllProducts(fromServer)
            _productDataList.postValue(response)
        }
    }


}