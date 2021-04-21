package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import org.json.JSONArray
import javax.inject.Inject

class CloseCacheRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun transactionDelete(id: Long) = remote.transactionSummeryDelete(id)
    suspend fun transactionUpdate(id: Long ,casheirId: Int ,description: String ,amount: String ,type: Int,paydeviceId: Int) = remote.transactionSummeryUpdate(id, casheirId, description, amount, type ,paydeviceId)
    suspend fun transactionClose(ids: String, casheirId: Int, recivecards: Int, resendcards: Int) = remote.transactionClose(ids, casheirId, recivecards, resendcards)
    suspend fun transactionSummeryAdd(casheirId: Int ,description: String ,amount: String ,type: Int,paydeviceId: Int) = remote.transactionSummeryAdd( casheirId, description, amount, type ,paydeviceId)
    suspend fun getPayDevices() = remote.getPayDevices()

}