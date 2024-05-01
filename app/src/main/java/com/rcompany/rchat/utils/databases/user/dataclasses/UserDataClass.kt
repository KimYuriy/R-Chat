package com.rcompany.rchat.utils.databases.user.dataclasses

import org.json.JSONObject

data class UserDataClass(
    var public_id: String,
    var email: String,
    var first_name: String,
    var last_name: String,
    var avatar_photo_url: String?
) {
    companion object {
        fun fromJson(json: JSONObject): UserDataClass {
            return UserDataClass(
                json.getString("public_id"),
                json.getString("email"),
                json.getString("first_name"),
                json.getString("last_name"),
                json.optString("avatar_photo_url")
            )
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("public_id", public_id)
        json.put("email", email)
        json.put("first_name", first_name)
        json.put("last_name", last_name)
        json.put("avatar_photo_url", avatar_photo_url)
        return json
    }
}