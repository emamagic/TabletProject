package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class RelationRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun setUserRelation(userID: Long ,relatedUser: Long ,type: Int) = remote.setUserRelation(userID, relatedUser, type)

}