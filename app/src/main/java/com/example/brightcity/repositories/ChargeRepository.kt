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
    suspend fun play(factoritemId: Long ,factorId: Long)   = remote.play(factoritemId, factorId)
    suspend fun pause(factoritemId: Long ,factorId: Long)  = remote.pause(factoritemId, factorId)
    suspend fun delete(factoritemId: Long ,factorId: Long) = remote.delete(factoritemId, factorId)
    suspend fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String ,paydeviceId: Int? = null)
        = remote.transactionAdd(userID, user_factorId, title, price, cash, cart, offCodID, paydeviceId)
    suspend fun productList() = remote.productList()
    suspend fun itemsList(factorId: Long) = remote.itemsList(factorId)
    suspend fun addProduct(id: Long ,pid: Long ,ord: Int,name: String ,awardId: Long ,price: String ,description: String, conditions: String ,fileId: String)
        = remote.addProduct(id, pid, ord, name, awardId, price, description, conditions, fileId)

}