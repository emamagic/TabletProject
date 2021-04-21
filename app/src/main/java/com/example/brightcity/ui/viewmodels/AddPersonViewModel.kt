package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.*
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.AddPersonRepository
import com.example.brightcity.repositories.RelationRepository
import com.example.brightcity.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    private val repository: AddPersonRepository
):ViewModel() {

    private val _addPerson = SingleLiveEvent<ApiWrapper<UserAddResponse>>()
    val addPerson: LiveData<ApiWrapper<UserAddResponse>>
        get() = _addPerson

    private val _userRelation = SingleLiveEvent<ApiWrapper<List<GetRelationResponse>>>()
    val userRelation: LiveData<ApiWrapper<List<GetRelationResponse>>>
        get() = _userRelation

    private var _userStatus = SingleLiveEvent<ApiWrapper<UserStatusResponse>>()
    val userStatus: LiveData<ApiWrapper<UserStatusResponse>>
        get() = _userStatus

    private var _userList = SingleLiveEvent<ApiWrapper<List<UserListResponse>>>()
    val userList: LiveData<ApiWrapper<List<UserListResponse>>>
        get() = _userList

    private val _setRelation = SingleLiveEvent<ApiWrapper<SetRelationResponse>>()
    val setRelation: LiveData<ApiWrapper<SetRelationResponse>>
        get() = _setRelation


    fun setRelation(userID: Long ,relatedUser: Long ,type: Int) = viewModelScope.launch {
        _setRelation.value = repository.setUserRelation(userID, relatedUser, type)
    }

    fun userAdd(name: String ,family: String ,birthDay: String ,gender: Int ,mobile: String ,nationalID: String ,description: String,
                isParent: Int) = viewModelScope.launch {
        _addPerson.value = repository.addPerson(name, family, birthDay, gender, mobile, nationalID, description, isParent)
    }


    fun getUserStatus(userID: Long? = null) = viewModelScope.launch {
        _userStatus.value = repository.userStatus(userID)
    }

    fun getUserRelations(userID: Long) = viewModelScope.launch {
        _userRelation.value = repository.getRelations(userID)
    }

    fun getUserList(num: Int ,page: Int ,search: String? = null ,asc: String? = null,order: String? = null) = viewModelScope.launch {
        _userList.value = repository.getUserList(num = num ,page = page ,search, asc ,order = order)
    }
}