package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class AddPersonRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun addPerson(name: String ,family: String ,birthDay: String ,gender: Int ,mobile: String ,nationalID: String ,description: String,
                          isParent: Int ) = remote.userAdd(name, family, birthDay, gender, mobile, nationalID, description, isParent)

    suspend fun userStatus(userID: Long? = null) = remote.userStatus(userID)

    suspend fun getRelations(userID: Long) = remote.getUserRelation(userID)

    suspend fun getUserList(num: Int ,page: Int ,search: String? = null ,asc: String? = null,order: String? = null) = remote.getUserList(num = num ,page = page ,search = search ,asc = asc ,order = order)

    suspend fun setUserRelation(userID: Long ,relatedUser: Long ,type: Int) = remote.setUserRelation(userID, relatedUser, type)


}