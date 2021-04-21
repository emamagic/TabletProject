package com.example.brightcity.api.responses

data class DeleteUserResponse(
    val id: Long,
    val username: String,
    val role: String,
    val name: String,
    val family: String,
    val status: Int,
    val gender: Int,
    val national_id: String,
    val photo_thumbnail: String,
    val is_parent: Int
)