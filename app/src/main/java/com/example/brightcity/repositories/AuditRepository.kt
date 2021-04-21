package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class AuditRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    suspend fun transactionList() = remote.transactionList()

}