package com.example.brightcity.repositories

import com.example.brightcity.sourses.RemoteDataSource
import javax.inject.Inject

class IncentiveRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    suspend fun getProductList() = remote.productList()

    suspend fun addProduct(productId: Long ,factorId: Long)
            = remote.addProduct(productId, factorId)

}