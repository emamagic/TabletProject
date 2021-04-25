package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import okhttp3.RequestBody
import javax.inject.Inject

class UserAccountRepository @Inject constructor(
    private val remote: RemoteDataSource
) {
    suspend fun userStatus(userID: Long? = null) = remote.userInfo(userID)
    suspend fun updateInfo(name: String? = null ,family: String? = null ,birthDay: String? = null ,gender: Int? = null ,mobile: Long? = null ,nationalID: Long? = null ,description: String? = null ,isParent: Int? = null) = remote.updateInfoSelf(name, family, birthDay, gender, mobile, nationalID, description, isParent)

}