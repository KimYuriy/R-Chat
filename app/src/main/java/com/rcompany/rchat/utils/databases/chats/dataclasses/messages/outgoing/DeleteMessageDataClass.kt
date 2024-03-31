package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing

import org.json.JSONObject

data class DeleteMessageDataClass(
    var id: String,
    var chat_id: String
) {
    fun toJson() = JSONObject().apply {
        put("id", id)
        put("chat_id", chat_id)
    }
}
