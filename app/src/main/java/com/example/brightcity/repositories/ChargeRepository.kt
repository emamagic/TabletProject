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


}