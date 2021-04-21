package com.example.brightcity.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.brightcity.api.MyApi
import com.example.brightcity.api.responses.UserListResponse
import retrofit2.HttpException
import java.io.IOException

class UserListPaging(
    private val myApi: MyApi,
    private val search: String? = null,
    private val order: String? = null,
    private val asc: String? = null
): PagingSource<Int, UserListResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserListResponse> {
        val position = params.key ?: 1
        return try {
            val response = myApi.getUserList(search , params.loadSize , position ,order ,asc)
            LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position -1,
                nextKey = if (response.isEmpty()) null else position +1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserListResponse>): Int? {
        return state.anchorPosition
    }

}