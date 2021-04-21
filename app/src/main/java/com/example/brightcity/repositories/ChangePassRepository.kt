package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class ChangePassRepository @Inject constructor(
    private val remote: RemoteDataSource
) {


    suspend fun changePass(currentPass: String ,newPass: String ,retype: String) = remote.changePass(currentPass, newPass, retype)
}