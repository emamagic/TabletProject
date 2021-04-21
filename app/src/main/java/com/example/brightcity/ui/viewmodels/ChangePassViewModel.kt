package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.ChangePassResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.ChangePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePassViewModel @Inject constructor(
    private val repository: ChangePassRepository
): ViewModel() {

    private var _changePass = MutableLiveData<ApiWrapper<ChangePassResponse>>()
    val changePass: LiveData<ApiWrapper<ChangePassResponse>>
        get() = _changePass

    fun changePass(currentPass: String ,newPass: String ,retype: String) = viewModelScope.launch {
        _changePass.value = repository.changePass(currentPass, newPass, retype)
    }

}