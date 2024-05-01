package com.rcompany.rchat.utils.databases.chats.dataclasses.common

import org.json.JSONObject

data class SenderDataClass(
    var user_id: String? = null, // Если отправитель сообщения - пользователь
    var chat_id: String? = null, // Если отправитель сообщения - канал
    var name: String,
    var avatar_photo_url: String? = null
) {
    companion object {
        fun fromJson(json: JSONObject): SenderDataClass {
            return SenderDataClass(
                json.optString("user_id"),
                json.optString("chat_id"),
                json.getString("name"),
                json.optString("avatar_photo_url")
            )
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("user_id", user_id)
        json.put("chat_id", chat_id)
        json.put("name", name)
        json.put("avatar_photo_url", avatar_photo_url)
        return json
    }
}