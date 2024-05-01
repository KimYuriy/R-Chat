package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing

import android.util.Log
import org.json.JSONObject

/**
 * Дата-класс отправленного сообщения
 * @property chat_id идентификатор чата типа [String]
 */
data class NewMessageDataClass(
    var chat_id: String? = null,
    var other_user_public_id: String? = null,
    var message_text: String? = null,
    var sender_user_id: String? = null,
    var reply_to_message_id: String? = null,
    var forwarded_message_id: String? = null,
    var is_silent: Boolean = false,
    var is_work_chat: Boolean? = null
) {

    /**
     * Функция создания json-объекта отправленного сообщения
     * @return json-объект отправленного сообщения типа [JSONObject]
     */
    fun toJson(): JSONObject {
        val json = JSONObject().apply {
            put("chat_id", chat_id)
            put("other_user_public_id", other_user_public_id)
            put("message_text", message_text)
            put("sender_user_id", sender_user_id)
            put("reply_to_message_id", reply_to_message_id)
            put("forwarded_message_id", forwarded_message_id)
            put("is_silent", is_silent)
            put("type", is_work_chat)
        }
        Log.d("NewMessageDataClass", "Data to be sent: $json")
        return json
    }
}