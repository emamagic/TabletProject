package com.example.brightcity.api.responses

data class TransactionListResponse(
    val award: Int,
    val cart: String,
    val cash: Int,
    val date: String,
    val id: Long,
    val info: String,
    val off: Int,
    val price: Int,
    val title: String,
    val userId: Int,
    val bon: Int,
    val userStatus: Int
)