package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.*
import com.example.brightcity.api.responses.DateTimeResponse
import com.example.brightcity.api.responses.LoginResponse
import com.example.brightcity.api.responses.UserLogout
import com.example.brightcity.api.responses.UserStatusResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.DashboardRepository
import com.example.brightcity.repositories.HomeRepository
import com.example.brightcity.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val repository: HomeRepository
): ViewModel(), LifecycleObserver {

    private val _userLogout = SingleLiveEvent<ApiWrapper<UserLogout>>()
    val userLogout: LiveData<ApiWrapper<UserLogout>>
        get() = _userLogout

    private val _userStatus = MutableLiveData<ApiWrapper<UserStatusResponse>>()
    val userStatus: LiveData<ApiWrapper<UserStatusResponse>>
        get() = _userStatus

    private val _date = MutableLiveData<ApiWrapper<DateTimeResponse>>()
    val date: LiveData<ApiWrapper<DateTimeResponse>>
        get() = _date

    fun userLogout() = viewModelScope.launch {
        _userLogout.value = repository.userLogout()
    }


    fun userStatus() = viewModelScope.launch {
        _userStatus.value = repository.userStatus()
    }

    fun getDateTime() = viewModelScope.launch {
        _date.value = repository.getDateTime()
    }

}