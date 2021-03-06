package com.example.brightcity.sourses

import com.example.brightcity.api.MyApi
import com.example.brightcity.api.safe.SafeApi
import okhttp3.RequestBody
import org.json.JSONArray
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val myApi: MyApi
): SafeApi() {

    suspend fun userLogin(userName: String ,password: String) = safeApi { myApi.userLogin(userName, password) }
    suspend fun logList() = safeApi { myApi.logList() }
    suspend fun userLogout() = safeApi { myApi.userLogout() }
    suspend fun userDashboard() = safeApi { myApi.userDashboard() }
    suspend fun userInfo(userID: Long?) = safeApi { myApi.userInfo(userID) }
    suspend fun userStatus(userID: Long? = null) = safeApi { myApi.userStatus(userID) }
    suspend fun changePass(currentPass: String, newPass: String, retype: String) = safeApi { myApi.changePass(currentPass, newPass, retype) }
    suspend fun updateInfo(id: Long ,name: String? = null ,family: String? = null ,birthDay: String? = null ,gender: Int? = null ,mobile: String? = null,nationalID: Long? = null,
                           description: String? = null ,isParent: Int? = null) = safeApi { myApi.updateInfo(id ,name, family, birthDay, gender, mobile ,nationalID ,description ,isParent) }
    suspend fun updateInfoSelf(name: String? = null ,family: String? = null ,birthDay: String? = null ,gender: Int? = null ,mobile: Long? = null,nationalID: Long? = null,
                           description: String? = null ,isParent: Int? = null) = safeApi { myApi.updateInfoSelf(name, family, birthDay, gender, mobile ,nationalID ,description ,isParent) }
    suspend fun getUserList(num:Int? = null ,page:Int? = null ,search: String? = null ,asc: String? = null ,order: String? = null) = safeApi { myApi.getUserList(num = num , page = page ,search = search ,asc = asc ,order = order) }
    suspend fun transactionDashboard() = safeApi { myApi.transactionDashboard() }
    suspend fun transactionList() = safeApi { myApi.transactionList() }
    suspend fun userAdd(name: String ,family: String ,birthDay: String ,gender: Int ,mobile: String ,nationalID: String ,description: String,
                        isParent: Int ) = safeApi { myApi.userAdd(name ,family ,birthDay ,gender ,mobile ,nationalID ,description ,isParent) }
    suspend fun getUserRelation(userID: Long) = safeApi { myApi.getUserRelation(userID) }
    suspend fun setUserRelation(userID: Long ,relatedUser: Long ,type: Int) = safeApi { myApi.setUserRelation(userID, relatedUser, type) }
    suspend fun getLastMessage() = safeApi { myApi.getLastMessage() }
    suspend fun getDateTime() = safeApi { myApi.getDateTime() }
    suspend fun offCode(code: String) = safeApi { myApi.offCode(code) }
    suspend fun transactionAdd(userID: Long ,user_factorId: Long ,title: String ,price: String ,cash: String ,cart: String ,offCodID: String? = null ,paydeviceId: Int? = null) =
        safeApi { myApi.transactionAdd(userID ,user_factorId ,title ,price ,cash ,cart ,offCodID ,paydeviceId) }
    suspend fun transactionDelete(id: Long) = safeApi { myApi.transactionDelete(id) }
    suspend fun transactionUpdate(price: String ,cash: String ,cart: String ,offCodID: String) = safeApi { myApi.transactionUpdate(price ,cash ,cart ,offCodID) }
    suspend fun transactionClose(ids: String, casheirId: Int, recivecards: Int, resendcards: Int) = safeApi { myApi.transactionCloseCache(ids, casheirId, recivecards, resendcards) }
    suspend fun transactionSummeryUpdate(id: Long ,casheirId: Int ,description: String ,amount: String ,type: Int,paydeviceId: Int) = safeApi { myApi.transactionSummeryUpdate(id, casheirId, description, amount, type ,paydeviceId) }
    suspend fun transactionSummeryDelete(id: Long) = safeApi { myApi.transactionSummeryDelete(id) }
    suspend fun transactionSummeryAdd(casheirId: Int ,description: String ,amount: String ,type: Int ,paydeviceId: Int) = safeApi { myApi.transactionSummeryAdd(casheirId, description, amount, type ,paydeviceId) }
    suspend fun getPayDevices() = safeApi { myApi.getPayDevices() }
    suspend fun deleteUser(id: Long) = safeApi { myApi.deleteUser(id) }
    suspend fun getFactor(userId: Long) = safeApi { myApi.getFactor(userId) }
    suspend fun addCharge(price: String ,factorId: Long) = safeApi { myApi.addCharge(price ,factorId) }
    suspend fun addOffCode(price: String ,factorId: Long) = safeApi { myApi.addOffCode(price ,factorId) }
    suspend fun play(factoritemId: Long ,factorId: Long) = safeApi { myApi.play(factoritemId ,factorId) }
    suspend fun pause(factoritemId: Long ,factorId: Long) = safeApi { myApi.pause(factoritemId ,factorId) }
    suspend fun delete(factoritemId: Long ,factorId: Long) = safeApi { myApi.deleteItem(factoritemId ,factorId) }
    suspend fun productList(page: Int? = null ,num: Int? = null) = safeApi { myApi.productList(page, num) }
    suspend fun itemsList(factorId: Long) = safeApi { myApi.getItems(factorId) }
    suspend fun addProduct(productId: Long ,factorId: Long) =
        safeApi { myApi.addProduct(productId, factorId) }
    suspend fun deleteRelation(userId: Long ,relatedUser: Long) = safeApi { myApi.deleteRelation(userId, relatedUser) }


}