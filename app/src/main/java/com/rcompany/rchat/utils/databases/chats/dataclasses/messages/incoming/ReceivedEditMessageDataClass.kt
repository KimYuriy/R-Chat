package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import com.rcompany.rchat.utils.extensions.time2HumanReadable
import org.json.JSONObject

data class ReceivedEditMessageDataClass(
    var id: String,
    var chat_id: String,
    var message_text: String,
    var reply_to_message: String? = null,
    var forwarded_message: String? = null,
    var edited_at: String
) {
    companion object {
        fun fromJson(json: JSONObject) : ReceivedEditMessageDataClass {
            return ReceivedEditMessageDataClass(
                json.getString("id"),
                json.getString("chat_id"),
                json.getString("message_text"),
                json.optString("reply_to_message"),
                json.optString("forwarded_message"),
                time2HumanReadable(json.getString("edited_at"))
            )
        }
    }
}