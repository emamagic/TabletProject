package com.example.brightcity.api.responses

data class ItemsListResponse(
    val id: Long,
    val title: String,
    val productId: Int,
    val count: Int,
    val unit: Int,
    val type: Int,
    val status: Int,
    val price: String,
    val percent: String,
    val remain: String,
    val fileId: Long
)