package com.example.brightcity.api.responses

data class OffCodeResponse(
    val id: Long,
    val percent: Long,
    val maxprice: Long,
    val description: String,
    val conditions: String
)