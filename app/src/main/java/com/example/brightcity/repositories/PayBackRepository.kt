package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class PayBackRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun addTransaction(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int) =
        remote.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)

}