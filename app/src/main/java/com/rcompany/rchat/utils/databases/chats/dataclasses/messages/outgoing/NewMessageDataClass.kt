package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing

import org.json.JSONObject

/**
 * Дата-класс отправленного сообщения
 * @property chat_id идентификатор чата типа [String]
 */
data class NewMessageDataClass(
    var chat_id: String,
    var message_text: String?,
    var sender_user_id: String,
    var reply_to_message: String? = null,
    var forwarded_message: String? = null,
    var is_silent: Boolean = false,
) {

    /**
     * Функция создания json-объекта отправленного сообщения
     * @return json-объект отправленного сообщения типа [JSONObject]
     */
    fun toJson() = JSONObject().apply {
        put("chat_id", chat_id)
        put("message_text", message_text)
        put("sender_user_id", sender_user_id)
        put("reply_to_message", reply_to_message)
        put("forwarded_message", forwarded_message)
        put("is_silent", is_silent)
    }
}
