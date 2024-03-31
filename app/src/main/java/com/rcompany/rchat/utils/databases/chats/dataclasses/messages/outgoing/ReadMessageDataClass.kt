package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing

import org.json.JSONObject

data class ReadMessageDataClass(
    var id: String,
    var chat_id: String,
    var read_by: String,
) {
    fun toJson() = JSONObject().apply {
        put("id", id)
        put("chat_id", chat_id)
        put("read_by", read_by)
    }
}