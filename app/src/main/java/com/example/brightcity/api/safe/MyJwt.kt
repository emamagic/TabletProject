package com.example.brightcity.api.safe

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.brightcity.api.MyApi
import javax.inject.Provider

object MyJwt {

    private const val JWT_SHARED_PREFERENCE_NAME = "jtw_pref"
    private const val LOGIN_TOKEN_STATUS = "LOGIN_TOKEN_STATUS"
    private const val ACCESS_TOKEN_STATUS = "ACCESS_TOKEN_STATUS"
    private const val REFRESH_TOKEN_STATUS = "REFRESH_TOKEN_STATUS"
    private const val AUTHORIZATION_STATUS = "AUTHORIZATION_STATUS"
    private const val LOGIN_TOKEN = "Y2FzaGVpcjpvZl9hc2RmYXNmYVNGa2RERlNhc2RmYXNkZmxha2o="
    private var pref: SharedPreferences? = null
    
    fun getSharedPref(context: Context) {
        pref = context.getSharedPreferences(JWT_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)!!
    }
    
    fun setAuthoriseToken(accessToken: String? ,refreshToken: String?){
        val pref = pref?.edit()
        pref?.putString(AUTHORIZATION_STATUS ,ACCESS_TOKEN_STATUS)?.apply()
        pref?.putString(ACCESS_TOKEN_STATUS, accessToken)?.apply()
        pref?.putString(REFRESH_TOKEN_STATUS, refreshToken)?.apply()
    }

    fun authoriseStatus(): String{
        return when (pref?.getString(AUTHORIZATION_STATUS ,LOGIN_TOKEN_STATUS)) {
            ACCESS_TOKEN_STATUS -> {
                pref?.edit()?.putString(AUTHORIZATION_STATUS , REFRESH_TOKEN_STATUS)?.apply()
                REFRESH_TOKEN_STATUS
            }
            REFRESH_TOKEN_STATUS -> {
                pref?.edit()?.putString(AUTHORIZATION_STATUS , LOGIN_TOKEN_STATUS)?.apply()
                LOGIN_TOKEN_STATUS
            }
            else -> {
                Log.e("TAG" ,"Undefine AuthoriseStatus")
                ""
            }
        }
    }

    fun setLoginToken(loginToken: String){
        pref?.edit()?.putString(LOGIN_TOKEN_STATUS, loginToken)?.apply()
    }

    fun getAccessToken(): String? {
        return pref?.getString(ACCESS_TOKEN_STATUS, null)
    }

    fun getLoginToken(): String? {
        return pref?.getString(LOGIN_TOKEN_STATUS ,LOGIN_TOKEN)
    }

    fun getRefreshToken(): String? {
        if (pref?.getString(REFRESH_TOKEN_STATUS ,null) == null) throw NoToken("Null Value For Login Token")
        return pref?.getString(REFRESH_TOKEN_STATUS, null)
    }

    fun isLoggedIn(): Boolean = getAccessToken() != null


    fun updatedToken(myApi: Provider<MyApi>){
        val data = myApi.get().sendRefreshToken().execute().body()
        setAuthoriseToken(data?.access_token ,data?.refresh_token)
    }

    fun setHeaderToken(): String {
        return when(pref?.getString(AUTHORIZATION_STATUS ,LOGIN_TOKEN_STATUS)) {
            LOGIN_TOKEN_STATUS -> getLoginToken()!!
            ACCESS_TOKEN_STATUS -> getAccessToken() ?: getLoginToken()!!
            else -> getRefreshToken()!! /* REFRESH_TOKEN_STATUS */
        }
    }

    fun clear(){
        pref?.edit()?.putString(ACCESS_TOKEN_STATUS ,null)?.apply()
        pref?.edit()?.clear()?.apply()
    }

}