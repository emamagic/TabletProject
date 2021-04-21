package com.example.brightcity.repositories

import com.example.brightcity.api.responses.DashboardResponse
import com.example.brightcity.api.responses.LogListResponse
import com.example.brightcity.api.safe.ApiWrapper
import com.example.brightcity.sourses.PagingDataSource
import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun getLogList(): ApiWrapper<List<LogListResponse>> = remote.logList()
    suspend fun userDashboard(): ApiWrapper<DashboardResponse> = remote.userDashboard()
    suspend fun getUserList(num: Int ,page: Int ,search: String? = null) = remote.getUserList(num, page ,search = search)
    suspend fun transactionDashboard() = remote.transactionDashboard()
    suspend fun getLastMessage() = remote.getLastMessage()

}