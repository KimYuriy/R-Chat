package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import org.json.JSONObject

data class ReceivedReadMessageDataClass(
    var id: String,
    var chat_id: String,
    var read_by: String,
) {
    companion object {
        fun fromJson(json: JSONObject) = ReceivedReadMessageDataClass(
            json.getString("id"),
            json.getString("chat_id"),
            json.getString("read_by"),
        )
    }
}
