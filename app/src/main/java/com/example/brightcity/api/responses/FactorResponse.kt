package com.example.brightcity.api.responses

data class FactorResponse(
    val id: Long,
    val userId: Long,
    val status: Int,
    val sumprice: Double,
    val payprice: Double,
    val vatprice: Double,
    val offprice: Double
)
