package com.example.brightcity.api.responses

data class ProductListResponse(
    val id: Long,
    val pid: Long,
    val ord: Int,
    val name: String,
    val awardId: Int,
    val price: String,
    val description: String,
    val condition: String,
    val fileId: Long
)