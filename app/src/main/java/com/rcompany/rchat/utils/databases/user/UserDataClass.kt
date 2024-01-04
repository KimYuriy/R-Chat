package com.rcompany.rchat.utils.databases.user

data class UserDataClass(
    var id: Int,
    var login: String,
    var avatar: String?
) {
    override fun toString() = "User ID: $id, login: $login, avatar: $avatar"
}