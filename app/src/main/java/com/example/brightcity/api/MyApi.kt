package com.example.brightcity.api

import com.example.brightcity.api.responses.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MyApi {

    @POST("user/login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("username") userName: String? = null,
        @Field("password") password: String? = null
    ): Response<LoginResponse>

    @POST("user/login")
    fun sendRefreshToken(): Call<LoginResponse>

    @GET("user/logout")
    suspend fun userLogout(): Response<UserLogout>

    @Multipart
    @POST("file/upload")
    suspend fun upload(@Part("file") file: RequestBody): ResponseBody

    @GET("logs/list")
    suspend fun logList(
        @Query("num") num: Int = 1
    ): Response<List<LogListResponse>>

    @POST("user/register")
    @FormUrlEncoded
    suspend fun userAdd(
        @Field("name") name: String,
        @Field("family") family: String,
        @Field("birthdate") birthday: String,
        @Field("gender") gender: Int,
        @Field("mobile") mobile: String,
        @Field("national_id") nationalId: String,
        @Field("description") description: String,
        @Field("is_parent") isParent: Int,
    ): Response<UserAddResponse>

    @GET("user/relations")
    suspend fun getUserRelation(@Query("userId") userID: Long): Response<List<GetRelationResponse>>

    @POST("user/relation")
    @FormUrlEncoded
    suspend fun setUserRelation(
        @Field("userId") userID: Long,
        @Field("related_user") relatedUser: Long,
        @Field("type") type: Int
    ): Response<SetRelationResponse>


    @GET("public/dateTime")
    suspend fun getDateTime(): Response<DateTimeResponse>

    @GET("transaction/dashboard")
    suspend fun transactionDashboard(): Response<TransactionDashboardResponse>

    @GET("transaction/list")
    suspend fun transactionList(): Response<List<TransactionListResponse>>

    @DELETE("transaction/delete/{id}")
    suspend fun delete(@Path("id") id: Int)

    @GET("user/dashboard")
    suspend fun userDashboard(): Response<DashboardResponse>

    @GET("user/status")
    suspend fun userStatus(
        @Query("userId") userID: Long? = null
    ): Response<UserStatusResponse>

    @GET("user/info")
    suspend fun userInfo(
        @Query("userId") userID: Long?
    ): Response<UserInfoResponse>

    @PUT("user/password")
    @FormUrlEncoded
    suspend fun changePass(
        @Field("current") currentPass: String,
        @Field("password") newPass: String,
        @Field("retype") retype: String
    ):Response<ChangePassResponse>

    @Multipart
    @PUT("user/update")
    suspend fun updateInfo(
        @Part("id") id: Long? = null,
        @Part("name") name: String? = null,
        @Part("family") family: String? = null,
        @Part("birthdate") birthDay: String? = "1",
        @Part("gender") gender: Int? = null,
        @Part("mobile") mobile: String? = null,
        @Part("national_id") national_id: Long? = null,
        @Part("description") description: String? = null,
        @Part("is_parent") is_parent: Int? = null,
    ): Response<UpdateInfoResponse>

    @Multipart
    @PUT("user/SelfUpdate")
    suspend fun updateInfoSelf(
        @Part("name") name: String? = null,
        @Part("family") family: String? = null,
        @Part("birthdate") birthDay: String? = "1",
        @Part("gender") gender: Int? = null,
        @Part("mobile") mobile: Long? = null,
        @Part("national_id") national_id: Long? = null,
        @Part("description") description: String? = null,
        @Part("is_parent") is_parent: Int? = null,
    ): Response<UpdateInfoResponse>

    @GET("user/list")
    suspend fun getUserList(
    @Query("search") search: String? = null,
    @Query("num") num: Int,
    @Query("page") page: Int,
    @Query("order") order: String? = null,
    @Query("num") asc: String? = null,
    ): List<UserListResponse>


    @GET("user/list")
    suspend fun getUserList(
        @Query("num") num: Int? = null,
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("asc") asc: String? = null,
        @Query("order") order: String? = null
    ): Response<List<UserListResponse>>

    @GET("message/list")
    suspend fun getLastMessage(): Response<List<LastMessageResponse>>

    @GET("transaction/offcode")
    suspend fun offCode(@Query("code") code: String): Response<OffCodeResponse>

    @POST("transaction/add")
    @FormUrlEncoded
    suspend fun transactionAdd(
        @Field("userId") userId: Long,
        @Field("user_factorId") user_factorId: Long,
        @Field("title") title: String,
        @Field("price") price: String,
        @Field("cash") cash: String,
        @Field("cart") cart: String,
        @Field("offcodeId") offCodeID: String,
        @Field("paydeviceId") paydeviceId: Int? = null
    ): Response<TransactionAddResponse>


    @DELETE("transaction/delete")
    @FormUrlEncoded
    suspend fun transactionDelete(@Field("id") id: Long): Response<TransactionDeleteResponse>

    @PUT("transaction/update")
    @FormUrlEncoded
    suspend fun transactionUpdate(
        @Field("price") price: String,
        @Field("cash") cash: String,
        @Field("cart") cart: String,
        @Field("offcodeId") offCodeID: String
    ): Response<TransactionUpdateResponse>

    @POST("transaction/close")
    @FormUrlEncoded
    suspend fun transactionCloseCache(
        @Field("ids") ids: String,
        @Field("casheirId") casheirId: Int,
        @Field("recivecards") recivecards: Int,
        @Field("resendcards") resendcards: Int,
    ): Response<CloseCacheResponse>

    @POST("transaction/addSummary")
    @FormUrlEncoded
    suspend fun transactionSummeryAdd(
        @Field("casheirId") casheirId: Int,
        @Field("description") description: String,
        @Field("amount") amount: String,
        @Field("type") type: Int,
        @Field("paydeviceId") paydeviceId: Int
    ): Response<AddSummeryTransactionResponse>

    @DELETE("transaction/deleteSummary")
    suspend fun transactionSummeryDelete(@Query("id") id: Long): Response<TransactionDeleteResponse>

    @PUT("transaction/editSummary")
    @FormUrlEncoded
    suspend fun transactionSummeryUpdate(
        @Field("id") id: Long,
        @Field("casheirId") casheirId: Int,
        @Field("description") description: String,
        @Field("amount") amount: String,
        @Field("type") type: Int,
        @Field("paydeviceId") paydeviceId: Int
    ): Response<TransactionUpdateResponse>

    @GET("transaction/paydevices")
    suspend fun getPayDevices(): Response<List<PayDevicesResponse>>

    @DELETE("user/delete/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    ): Response<DeleteUserResponse>


    @GET("factor/getfactor")
    suspend fun getFactor(
        @Query("userId") userId: Long
    ): Response<FactorResponse>

    @POST("factor/addcharge")
    @FormUrlEncoded
    suspend fun addCharge(
        @Field("price") price: String,
        @Field("factorId") factorId: Long
    ): Response<AddChargeResponse>

    @POST("factor/addoffcode")
    @FormUrlEncoded
    suspend fun addOffCode(
        @Field("code") code: String,
        @Field("factorId") factorId: Long
    ): Response<AddOffCodeResponse>

    @POST("factor/play")
    @FormUrlEncoded
    suspend fun play(
        @Field("factoritemId") factoritemId: Long,
        @Field("factorId") factorId: Long
    ): Response<PlayResponse>

    @POST("factor/pause")
    @FormUrlEncoded
    suspend fun pause(
        @Field("factoritemId") factorismId: Long,
        @Field("factorId") factorId: Long
    ): Response<PauseResponse>

    @DELETE("factor/deleteitem")
    suspend fun deleteItem(
        @Field("factoritemId") factorismId: Long,
        @Field("factorId") factorId: Long
    ): Response<DeleteItemResponse>

    @GET("product/list")
    suspend fun productList(
        @Query("page") page: Int? = null,
        @Query("num") num: Int? = null
    ): Response<List<ProductListResponse>>

    @GET("factor/items")
    suspend fun getItems(
        @Query("factorId") factorId: Long
    ): Response<List<ItemsListResponse>>

    @PUT("factor/addproduct")
    suspend fun addProduct(
        @Field("productId") productId: Long? = null,
        @Field("factorId") factorId: Long? = null
    ): Response<AddProductResponse>

}