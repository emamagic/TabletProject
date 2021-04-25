package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.TransactionAddResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
): ViewModel() {

    private val _transactionAdd = MutableLiveData<ApiWrapper<TransactionAddResponse>>()
    val transactionAdd: LiveData<ApiWrapper<TransactionAddResponse>>
        get() = _transactionAdd


    fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null)
            = viewModelScope.launch {
        _transactionAdd.value = repository.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    }

}