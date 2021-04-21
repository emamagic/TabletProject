package com.example.brightcity.api.responses

data class TransactionDashboardResponse(
    val sum_payed: Int,
    val sum_used: Int,
    val sum_back: Int,
    val sum_negative: Int
)
