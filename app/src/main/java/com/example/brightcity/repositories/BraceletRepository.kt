package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class BraceletRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    suspend fun getUserList(search: String? = null) = remote.getUserList(search = search)
}