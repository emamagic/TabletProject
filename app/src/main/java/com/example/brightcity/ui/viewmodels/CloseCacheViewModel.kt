package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.*
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.CloseCacheRepository
import com.example.brightcity.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class CloseCacheViewModel @Inject constructor(
    private val repository: CloseCacheRepository
):ViewModel() {

    private val _transactionDelete = SingleLiveEvent<ApiWrapper<TransactionDeleteResponse>>()
    val transactionDelete: LiveData<ApiWrapper<TransactionDeleteResponse>>
        get() = _transactionDelete

    private val _transactionUpdate = SingleLiveEvent<ApiWrapper<TransactionUpdateResponse>>()
    val transactionUpdate: LiveData<ApiWrapper<TransactionUpdateResponse>>
        get() = _transactionUpdate

    private val _transactionClose = SingleLiveEvent<ApiWrapper<CloseCacheResponse>>()
    val transactionClose: LiveData<ApiWrapper<CloseCacheResponse>>
        get() = _transactionClose

    private val _transactionAdd = SingleLiveEvent<ApiWrapper<AddSummeryTransactionResponse>>()
    val transactionAdd: LiveData<ApiWrapper<AddSummeryTransactionResponse>>
        get() = _transactionAdd

    private val _payDevices = SingleLiveEvent<ApiWrapper<List<PayDevicesResponse>>>()
    val payDevices: LiveData<ApiWrapper<List<PayDevicesResponse>>>
        get() = _payDevices


    fun transactionDelete(id: Long) = viewModelScope.launch {
        _transactionDelete.value = repository.transactionDelete(id)
    }

    fun transactionUpdate(id: Long ,casheirId: Int ,description: String ,amount: String ,type: Int,paydeviceId: Int) = viewModelScope.launch {
        _transactionUpdate.value = repository.transactionUpdate(id, casheirId, description, amount, type ,paydeviceId)
    }

    fun transactionClose(ids: String, casheirId: Int, recivecards: Int, resendcards: Int) = viewModelScope.launch {
        _transactionClose.value = repository.transactionClose(ids, casheirId, recivecards, resendcards)
    }

    fun transactionAdd(casheirId: Int ,description: String ,amount: String ,type: Int ,paydeviceId: Int) = viewModelScope.launch {
        _transactionAdd.value = repository.transactionSummeryAdd(casheirId, description, amount, type ,paydeviceId)
    }

    fun getPayDevices() = viewModelScope.launch {
        _payDevices.value = repository.getPayDevices()
    }

}