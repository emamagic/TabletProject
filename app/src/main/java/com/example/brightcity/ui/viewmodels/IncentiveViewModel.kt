package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.AddProductResponse
import com.example.brightcity.api.responses.ProductListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.IncentiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncentiveViewModel @Inject constructor(
    private val repository: IncentiveRepository
): ViewModel() {

    private val _productList = MutableLiveData<ApiWrapper<List<ProductListResponse>>>()
    val productList: LiveData<ApiWrapper<List<ProductListResponse>>>
        get() = _productList

    private val _addProduct = MutableLiveData<ApiWrapper<AddProductResponse>>()
    val addProduct: LiveData<ApiWrapper<AddProductResponse>>
        get() = _addProduct



    fun productList() = viewModelScope.launch {
        _productList.value = repository.getProductList()
    }
    fun addProduct(productId: Long ,factorId: Long) = viewModelScope.launch {
        _addProduct.value = repository.addProduct(productId, factorId)
    }

}