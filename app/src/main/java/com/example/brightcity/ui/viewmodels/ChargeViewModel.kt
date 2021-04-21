package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.FactorResponse
import com.example.brightcity.api.responses.UserInfoResponse
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

    fun getUserInfo(userId: Long?) = viewModelScope.launch {
        _userInfo.value = repository.getUserInfo(userId)
    }

    fun getFactor(userId: Long) = viewModelScope.launch {
        _factor.value = repository.getFactor(userId)
    }


}