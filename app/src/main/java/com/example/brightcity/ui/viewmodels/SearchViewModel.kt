package com.example.brightcity.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.brightcity.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): ViewModel() {

     fun getUserList(search: String? = null ,order: String? = null ,asc: String? = null) = repository.getUserList(search, order, asc).asLiveData(viewModelScope.coroutineContext)

}