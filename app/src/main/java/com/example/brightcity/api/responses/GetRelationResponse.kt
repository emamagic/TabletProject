package com.example.brightcity.api.responses

import com.google.gson.annotations.SerializedName

data class GetRelationResponse(
    val id: Long,
    val pid: Long,
    val ord: Int,
    val userId: Long,
    val related_userId: Long,
    val type: Int,
    val status: Int,
    val description: String,
    @SerializedName("user.fullname")
    val userName: String
){
    fun mapToUserList(): UserListResponse{
        return UserListResponse(
            id = userId,
            name = userName,
            status = status,
            type = type
        )
    }

}