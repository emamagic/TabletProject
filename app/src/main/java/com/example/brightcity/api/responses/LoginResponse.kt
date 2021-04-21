package com.example.brightcity.api.responses

data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)