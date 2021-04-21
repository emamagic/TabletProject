package com.example.brightcity.api.responses

import org.json.JSONObject

data class ErrorResponse(
    val statusCode: Int,
    val error: String,
    val message: String,
    val validation: Validation?
)

data class Validation(
    val headers: JSONObject?,
    val error: ErrorHeaders,
    val message: String?
)

data class ErrorHeaders(
    val source: String,
    val keys: List<String>,
    val message: String
)
