package com.example.brightcity.api.responses

data class GetRelationResponse(
    val id: Long,
    val pid: Long,
    val ord: Int,
    val userId: Long,
    val related_userId: Long,
    val type: Int,
    val status: Int,
    val description: String
)