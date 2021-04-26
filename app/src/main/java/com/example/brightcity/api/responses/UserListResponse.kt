package com.example.brightcity.api.responses

data class UserListResponse(
    val id: Long,
    val name: String? = null,
    val family: String? = null,
    val status: Int? = null,
    val role: String? = null,
    val birthdate: String? = null,
    val age: Int? = null,
    val gender: Int? = null,
    val mobile: String? = null,
    val national_id: String? = null,
    val credit: Long? = null,
    val gift: Long? = null,
    val level: Int? = null,
    val score: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val fileId: String? = null,
    // this below field is not come from server side
    var type: Int? = null
){
    override fun toString(): String {
        return "$type"
    }
}