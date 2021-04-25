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

    suspend fun productList(page: Int? = null ,num: Int? =null) = remote.productList(page, num)
    suspend fun itemsList(factorId: Long) = remote.itemsList(factorId)
    suspend fun addProduct(productId: Long ,factorId: Long)
        = remote.addProduct(productId, factorId)

}