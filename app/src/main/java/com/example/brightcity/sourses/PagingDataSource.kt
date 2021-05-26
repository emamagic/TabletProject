package com.example.brightcity.sourses

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.brightcity.api.MyApi
import com.example.brightcity.paging.UserListPaging
import javax.inject.Inject

class PagingDataSource @Inject constructor(
    private val myApi: MyApi
) {

    fun getUserList(search: String? = null, order: String? = null, asc: String? = null) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {UserListPaging(myApi ,search ,order ,asc)}
        ).flow

}