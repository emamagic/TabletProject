package com.example.brightcity.api.responses

data class UserListResponse(
    val id: Long,
    val name: String,
    val family: String,
    val status: Int,
    val role: String,
    val birthdate: String,
    val age: Int,
    val gender: Int,
    val mobile: String,
    val national_id: String,
    val credit: Long,
    val gift: Long,
    val level: Int,
    val score: Int,
    val createdAt: String,
    val updatedAt: String,
    val fileId: String? = null,
    // this below field is not come from server side
    var type: Int? = null
)