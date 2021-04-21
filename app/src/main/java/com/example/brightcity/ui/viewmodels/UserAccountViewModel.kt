package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.ChangePassResponse
import com.example.brightcity.api.responses.UpdateInfoResponse
import com.example.brightcity.api.responses.UserInfoResponse
import com.example.brightcity.api.responses.UserStatusResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.UserAccountRepository
import com.example.brightcity.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val repository: UserAccountRepository
): ViewModel() {

    private var _userStatus = MutableLiveData<ApiWrapper<UserInfoResponse>>()
    val userStatus: LiveData<ApiWrapper<UserInfoResponse>>
        get() = _userStatus

    private var _updateInfo = SingleLiveEvent<ApiWrapper<UpdateInfoResponse>>()
    val updateInfo: LiveData<ApiWrapper<UpdateInfoResponse>>
        get() = _updateInfo

    fun getUserStatus(userID: Long? = null) = viewModelScope.launch {
        _userStatus.value = repository.userStatus(userID)
    }


    fun updateInfo(name: String? = null ,family: String? = null ,birthDay: String? = null ,gender: Int? = null ,mobile: Long? = null ,nationalID: Long? = null ,description: String? = null ,isParent: Int? = null) =
        viewModelScope.launch {
            _updateInfo.value = repository.updateInfo(name, family, birthDay, gender, mobile, nationalID, description, isParent)
        }
/*    fun updateInfo(name: RequestBody? = null, family: RequestBody? = null, birthDay: RequestBody? = null, gender: RequestBody? = null, mobile: RequestBody? = null, nationalID: RequestBody? = null, description: RequestBody? = null, isParent: RequestBody? = null) =
        viewModelScope.launch {
            _updateInfo.value = repository.updateInfo(name, family, birthDay, gender, mobile, nationalID, description, isParent)
        }*/

}