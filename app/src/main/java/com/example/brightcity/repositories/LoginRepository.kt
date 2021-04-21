package com.example.brightcity.repositories

import com.example.brightcity.api.responses.LoginResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun userLogin(userName: String ,password: String): ApiWrapper<LoginResponse> = remote.userLogin(userName, password)
}