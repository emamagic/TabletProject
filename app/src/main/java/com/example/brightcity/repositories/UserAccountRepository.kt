package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import okhttp3.RequestBody
import javax.inject.Inject

class UserAccountRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    suspend fun userStatus(userID: Long? = null) = remote.userInfo(userID)
    suspend fun updateInfo(name: String? = null ,family: String? = null ,birthDay: String? = null ,gender: Int? = null ,mobile: Long? = null ,nationalID: Long? = null ,description: String? = null ,isParent: Int? = null) = remote.updateInfo(name, family, birthDay, gender, mobile, nationalID, description, isParent)
   // suspend fun updateInfo(name: RequestBody? = null, family: RequestBody? = null, birthDay: RequestBody? = null, gender: RequestBody? = null, mobile: RequestBody? = null, nationalID: RequestBody? = null, description: RequestBody? = null, isParent: RequestBody? = null) = remote.updateInfo(name, family, birthDay, gender, mobile, nationalID, description, isParent)

}