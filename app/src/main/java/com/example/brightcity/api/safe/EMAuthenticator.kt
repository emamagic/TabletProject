package com.example.brightcity.api.safe

import android.util.Log
import com.example.brightcity.api.MyApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class EMAuthenticator @Inject constructor(private val myApi: Provider<MyApi>) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {
        if (MyJwt.isLoggedIn()) {
            val authenticator = MyJwt.authoriseStatus()
            Log.e("TAG", "authenticate: $authenticator")
            if (authenticator == "REFRESH_TOKEN_STATUS") {
                MyJwt.updatedToken(myApi)
            }
        }
        return response.request
    }
}