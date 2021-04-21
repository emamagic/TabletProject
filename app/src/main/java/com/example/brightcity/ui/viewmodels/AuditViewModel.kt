package com.example.brightcity.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.brightcity.api.responses.TransactionListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.AuditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuditViewModel @Inject constructor(
    private val repository: AuditRepository
): ViewModel() {

    private var _transactionList = MutableLiveData<ApiWrapper<List<TransactionListResponse>>>()
    val transactionList: LiveData<ApiWrapper<List<TransactionListResponse>>>
        get() = _transactionList


    fun getTransactionList() = viewModelScope.launch {
        _transactionList.value = repository.transactionList()
    }
}