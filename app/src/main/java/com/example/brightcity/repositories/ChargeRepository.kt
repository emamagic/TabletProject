package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class ChargeRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun getUserInfo(userId: Long?) = remote.userInfo(userId)
    suspend fun getFactor(userId: Long) = remote.getFactor(userId)
    suspend fun addCharge(price: String ,factorId: Long)  = remote.addCharge(price, factorId)
    suspend fun addOffCode(price: String ,factorId: Long) = remote.addOffCode(price ,factorId)
    suspend fun play(factorismId: Long ,factorId: Long)   = remote.play(factorismId, factorId)
    suspend fun pause(factorismId: Long ,factorId: Long)  = remote.pause(factorismId, factorId)
    suspend fun delete(factorismId: Long ,factorId: Long) = remote.delete(factorismId, factorId)
    suspend fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int)
        = remote.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    suspend fun productList() = remote.productList()
    suspend fun itemsList(factorId: Long) = remote.itemsList(factorId)
    suspend fun addProduct(id: Long ,pid: Long ,ord: Int,name: String ,awardId: Long ,price: String ,description: String, conditions: String ,fileId: String)
        = remote.addProduct(id, pid, ord, name, awardId, price, description, conditions, fileId)

}