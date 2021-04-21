package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.UserListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.BraceletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BraceletViewModel @Inject constructor(
    private val repository: BraceletRepository
): ViewModel() {

    private var _userList = MutableLiveData<ApiWrapper<List<UserListResponse>>>()
    val userList: LiveData<ApiWrapper<List<UserListResponse>>>
        get() = _userList

    fun getUserList(search: String? = null) = viewModelScope.launch {
        _userList.value = repository.getUserList(search)
    }

}