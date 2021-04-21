package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.*
import com.example.brightcity.api.responses.*
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.DashboardRepository
import com.example.brightcity.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
): ViewModel(), LifecycleObserver {

    private val _logList = MutableLiveData< ApiWrapper<List<LogListResponse>>>()
    val logList: LiveData< ApiWrapper<List<LogListResponse>>>
        get() = _logList

    private val _userDashboard = MutableLiveData< ApiWrapper<DashboardResponse>>()
    val userDashboard: LiveData< ApiWrapper<DashboardResponse>>
        get() = _userDashboard

    private val _userList = SingleLiveEvent< ApiWrapper<List<UserListResponse>>>()
    val userList: LiveData< ApiWrapper<List<UserListResponse>>>
        get() = _userList

    private val _lastMessage = MutableLiveData<ApiWrapper<List<LastMessageResponse>>>()
    val lastMessage: LiveData< ApiWrapper<List<LastMessageResponse>>>
        get() = _lastMessage

    private val _transactionDashboard = MutableLiveData<ApiWrapper<TransactionDashboardResponse>>()
    val transactionDashboard: LiveData< ApiWrapper<TransactionDashboardResponse>>
        get() = _transactionDashboard

    fun getLogList() = viewModelScope.launch {
        _logList.value = repository.getLogList()
    }

    fun userDashboard() = viewModelScope.launch {
        _userDashboard.value = repository.userDashboard()
    }

    fun getUserList(num: Int ,page: Int ,search: String? = null) = viewModelScope.launch {
        _userList.value = repository.getUserList(num, page ,search = search)
    }

    fun getTransactionDashboard() = viewModelScope.launch {
        _transactionDashboard.value = repository.transactionDashboard()
    }

    fun getLastMessage() = viewModelScope.launch {
        _lastMessage.value = repository.getLastMessage()
    }

}