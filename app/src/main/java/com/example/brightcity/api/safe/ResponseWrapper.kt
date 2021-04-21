package com.example.brightcity.api.safe

class ResponseWrapper {

    private var code: Int? = null

    private fun setCode(code: Int){ this.code = code }

    private fun getCode(): Int = this.code!!
}