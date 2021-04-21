package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.*
import com.example.brightcity.api.responses.LoginResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel(), LifecycleObserver {

    private val _userLogin = MutableLiveData<ApiWrapper<LoginResponse>>()
    val userLogin: LiveData<ApiWrapper<LoginResponse>>
        get() = _userLogin


    fun userLogin(userName: String ,password: String) = viewModelScope.launch {
        _userLogin.value = repository.userLogin(userName, password)
    }


}