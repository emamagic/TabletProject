package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.*
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.ChargeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargeViewModel @Inject constructor(
    private val repository: ChargeRepository
): ViewModel() {

    private val _userInfo = MutableLiveData<ApiWrapper<UserInfoResponse>>()
    val userInfo: LiveData<ApiWrapper<UserInfoResponse>>
        get() = _userInfo

    private val _factor = MutableLiveData<ApiWrapper<FactorResponse>>()
    val factor: LiveData<ApiWrapper<FactorResponse>>
        get() = _factor

    private val _addCharge = MutableLiveData<ApiWrapper<AddChargeResponse>>()
    val addCharge: LiveData<ApiWrapper<AddChargeResponse>>
        get() = _addCharge

    private val _addOffCode = MutableLiveData<ApiWrapper<AddOffCodeResponse>>()
    val addOffCode: LiveData<ApiWrapper<AddOffCodeResponse>>
        get() = _addOffCode

    private val _play = MutableLiveData<ApiWrapper<PlayResponse>>()
    val play: LiveData<ApiWrapper<PlayResponse>>
        get() = _play

    private val _pause = MutableLiveData<ApiWrapper<PauseResponse>>()
    val pause: LiveData<ApiWrapper<PauseResponse>>
        get() = _pause

    private val _delete = MutableLiveData<ApiWrapper<DeleteItemResponse>>()
    val delete: LiveData<ApiWrapper<DeleteItemResponse>>
        get() = _delete

    private val _transactionAdd = MutableLiveData<ApiWrapper<TransactionAddResponse>>()
    val transactionAdd: LiveData<ApiWrapper<TransactionAddResponse>>
        get() = _transactionAdd

    private val _productList = MutableLiveData<ApiWrapper<List<ProductListResponse>>>()
    val productList: LiveData<ApiWrapper<List<ProductListResponse>>>
        get() = _productList

    private val _itemsList = MutableLiveData<ApiWrapper<List<ItemsListResponse>>>()
    val itemsList: LiveData<ApiWrapper<List<ItemsListResponse>>>
        get() = _itemsList

    private val _addProduct = MutableLiveData<ApiWrapper<AddProductResponse>>()
    val addProduct: LiveData<ApiWrapper<AddProductResponse>>
        get() = _addProduct


    fun getUserInfo(userId: Long?) = viewModelScope.launch {
        _userInfo.value = repository.getUserInfo(userId)
    }

    fun getFactor(userId: Long) = viewModelScope.launch {
        _factor.value = repository.getFactor(userId)
    }

    fun addCharge(price: String ,factorId: Long) = viewModelScope.launch {
        _addCharge.value = repository.addCharge(price, factorId)
    }

    fun addOffCode(price: String ,factorId: Long) = viewModelScope.launch {
        _addOffCode.value = repository.addOffCode(price, factorId)
    }

    fun play(factoritemId: Long ,factorId: Long) = viewModelScope.launch {
        _play.value = repository.play(factoritemId, factorId)
    }

    fun pause(factoritemId: Long ,factorId: Long) = viewModelScope.launch {
        _pause.value = repository.pause(factoritemId, factorId)
    }

    fun delete(factoritemId: Long ,factorId: Long) = viewModelScope.launch {
        _delete.value = repository.delete(factoritemId, factorId)
    }

    fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null)
    = viewModelScope.launch {
        _transactionAdd.value = repository.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }

    fun productList() = viewModelScope.launch {
        _productList.value = repository.productList()
    }

    fun itemsList(factorId: Long) = viewModelScope.launch {
        _itemsList.value = repository.itemsList(factorId)
    }

    fun addProduct(id: Long ,pid: Long ,ord: Int,name: String ,awardId: Long ,price: String ,description: String, conditions: String ,fileId: String) = viewModelScope.launch {
        _addProduct.value = repository.addProduct(id, pid, ord, name, awardId, price, description, conditions, fileId)
    }

}