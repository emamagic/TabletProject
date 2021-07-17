package com.example.brightcity.api.safe

import com.example.brightcity.api.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketException
import java.net.UnknownHostException

abstract class SafeApi() {

    suspend fun <T> safeApi(call: suspend () -> Response<T>): ApiWrapper<T> {

        return apiFiltering { call() }

    }


    private fun <T> handleResponse(response: Response<T>): ApiWrapper<T> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ApiWrapper.Success(
                    data = it,
                    headers = response.headers(),
                    code = response.code()
                )
            }
        }

            return ApiWrapper.ApiError(
                error = Gson().fromJson(response.errorBody()?.string() ,ErrorResponse::class.java),
                headers = response.headers()
            )
    }

    private suspend fun <T> apiFiltering(call: suspend () -> Response<T>): ApiWrapper<T> {
        return try {
            handleResponse(call())
        } catch (e: NoInternetException) {
            ApiWrapper.NetworkError(message = "${e.message}")
        } catch (e: UnknownHostException){
            ApiWrapper.NetworkError(message = "${e.message}")
        } catch (e: SocketException){
            ApiWrapper.NetworkError(message = "${e.message}")
        } catch (t: Throwable) {
                ApiWrapper.UnknownError(message = "${t.message}//${t.cause}")
        }
    }

}