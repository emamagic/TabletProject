package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brightcity.api.responses.LoginResponse
import com.example.brightcity.api.responses.SetRelationResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.repositories.RelationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelationViewModel @Inject constructor(
    private val repository: RelationRepository
): ViewModel() {


    private val _setRelation = MutableLiveData<ApiWrapper<SetRelationResponse>>()
    val setRelation: LiveData<ApiWrapper<SetRelationResponse>>
        get() = _setRelation


    fun setRelation(userID: Long ,relatedUser: Long ,type: Int) = viewModelScope.launch {
        _setRelation.value = repository.setUserRelation(userID, relatedUser, type)
    }

}