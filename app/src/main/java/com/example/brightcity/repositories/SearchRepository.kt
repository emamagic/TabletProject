package com.example.brightcity.repositories

import com.example.brightcity.sourses.PagingDataSource
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val paging: PagingDataSource
) {
      fun getUserList(search: String? = null ,order: String? = null ,asc: String? = null) = paging.getUserList(search, order, asc)
}