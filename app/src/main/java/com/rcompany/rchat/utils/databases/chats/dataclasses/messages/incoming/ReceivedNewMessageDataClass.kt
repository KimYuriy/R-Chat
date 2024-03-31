package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import com.rcompany.rchat.utils.extensions.time2HumanReadable
import org.json.JSONObject

/**
 * Дата-класс данных сообщения
 */
data class ReceivedNewMessageDataClass(
    var id: String,
    var chat_id: String,
    var message_text: String?,
    var sender_user_id: String,
    var reply_to_message: String? = null,
    var forwarded_message: String? = null,
    var is_silent: Boolean = false,
    var created_at: String
) {
    companion object {
        fun fromJson(source: JSONObject): ReceivedNewMessageDataClass {
            return ReceivedNewMessageDataClass(
                source.getString("id"),
                source.getString("chat_id"),
                source.optString("message_text"),
                source.getString("sender_user_id"),
                source.optString("reply_to_message"),
                source.optString("forwarded_message"),
                source.optBoolean("is_silent"),
                time2HumanReadable(source.getString("created_at"))
            )
        }
    }
}