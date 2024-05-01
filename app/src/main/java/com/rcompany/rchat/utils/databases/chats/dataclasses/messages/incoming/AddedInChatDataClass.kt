package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import org.json.JSONObject

data class AddedInChatDataClass(
    var chat_id: String,
    var name: String,
    var is_work_chat: Boolean,
    var avatar_photo_url: String? = null,
    var user_who_added: String
) {
    companion object {
        fun fromJson(json: JSONObject) = AddedInChatDataClass(
            json.getString("chat_id"),
            json.getString("name"),
            json.getBoolean("is_work_chat"),
            json.optString("avatar_photo_url"),
            json.getString("user_who_added")
        )
    }
}