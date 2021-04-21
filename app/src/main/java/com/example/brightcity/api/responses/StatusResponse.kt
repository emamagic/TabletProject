package com.example.brightcity.api.responses

data class StatusResponse(
    val id: Int,
    val username: String,
    val type: Int,
    val name: String,
    val family: String,
    val status: Int,
    val gender: Int,
    val national_id: Int,
    val photo_thumbnail: String,
    val is_parent: Int
)