package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class ChargeRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun getUserInfo(userId: Long?) = remote.userInfo(userId)

}