package com.example.brightcity.api.responses

data class UserStatusResponse(
    val family: String,
    val gender: Int,
    val id: Long,
    val is_parent: Int,
    val mobile: String,
    val name: String,
    val national_id: String,
    val birthdate: String,
    val photo_thumbnail: String,
    val role: String,
    val status: Int,
    val username: String,
    val fileId: String? = null,
    val description: String
)