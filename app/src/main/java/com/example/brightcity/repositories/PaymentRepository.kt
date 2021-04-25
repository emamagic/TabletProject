package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null)
            = remote.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
}