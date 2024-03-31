package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing

import org.json.JSONObject

data class EditMessageDataClass(
    var id: String,
    var chat_id: String,
    var message_text: String?,
    var reply_to_message: String? = null,
    var forwarded_message: String? = null,
) {
    fun toJson() = JSONObject().apply {
        put("id", id)
        put("chat_id", chat_id)
        put("message_text", message_text)
        put("reply_to_message", reply_to_message)
        put("forwarded_message", forwarded_message)
    }
}