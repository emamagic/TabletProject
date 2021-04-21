package com.example.brightcity.api.responses

import com.google.gson.annotations.SerializedName

data class LastMessageResponse(
    val updatedAt: String,
    val userId: Long,
    val text: String,
    val link: String,
    @SerializedName("user.fullname")
    val username: String
)