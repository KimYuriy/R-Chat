package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import org.json.JSONObject

data class ReceivedDeleteMessageDataClass(
    var id: String,
    var chat_id: String
) {
    companion object {
        fun fromJson(json: JSONObject): ReceivedDeleteMessageDataClass {
            return ReceivedDeleteMessageDataClass(
                json.getString("id"),
                json.getString("chat_id")
            )
        }
    }
}