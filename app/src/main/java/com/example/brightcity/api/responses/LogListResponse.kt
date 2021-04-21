package com.example.brightcity.api.responses

data class LogListResponse(
    val id: Int?,
    val userId: Int?,
    val type: String?,
    val value: String?,
    val status: Int?,
    val createdAt: String,
    val fileId: Long?
)