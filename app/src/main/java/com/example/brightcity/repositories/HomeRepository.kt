package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    suspend fun userLogout() = remote.userLogout()
    suspend fun userStatus() = remote.userStatus()
    suspend fun getDateTime() = remote.getDateTime()


}