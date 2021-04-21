package com.example.brightcity.api.safe

import com.example.brightcity.api.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketException
import java.net.UnknownHostException

abstract class SafeApi() {

   // private val mutex = Mutex()

    suspend fun <T> safeApi(call: suspend () -> Response<T>): ApiWrapper<T> {

/*        return if (!mutex.isLocked) mutex.withLock { apiTry { call.invoke() } }
        else   null*/
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
           // getNewRefreshToken(response ,application)
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

/*    private fun <T> getNewRefreshToken(response: Response<T> ,application: Application){
        if (response.code() == 401){
            val jwtHelper = JwtHelper.getInstance(application)
            if (jwtHelper.isLoggedIn){
                // ACCESS_TOKEN Deprecated
                jwtHelper.jwtStatus = REFRESH_TOKEN_STATUS
                GlobalScope.launch {
                    val data = Gson().fromJson(myApi.userLogin().body()?.toString() ,LoginResponse::class.java)
                    Log.e("TAG", "getNewRefreshToken: data -> $data")
                    jwtHelper.setToken(data.access_token ,ACCESS_TOKEN_STATUS)
                    jwtHelper.setToken(data.refresh_token ,REFRESH_TOKEN_STATUS)
                    cancel()
                    return@launch
                }
            }
        }
    }*/



}