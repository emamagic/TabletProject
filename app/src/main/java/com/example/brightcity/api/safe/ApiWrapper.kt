package com.example.brightcity.api.safe

import com.example.brightcity.api.responses.ErrorResponse
import okhttp3.Headers
import okhttp3.ResponseBody


sealed class ApiWrapper<T>(
    val data: T? = null,
    val error: ErrorResponse? = null,
    val headers: Headers? = null,
    val message: String? = null,
    val code: Int? = null
) {
    class Success <T> (data: T ,headers: Headers ,code: Int): ApiWrapper<T>(data ,null ,headers ,null ,code)
    class ApiError<T>(error: ErrorResponse?, headers: Headers): ApiWrapper<T>(null,error,headers ,null)
    class NetworkError<T>(message: String): ApiWrapper<T>(null,null,null ,message)
    class UnknownError<T>(message: String): ApiWrapper<T>(null,null,null ,message)
}