package com.rcompany.rchat.utils.databases.chats

import org.json.JSONObject

/**
 * Дата-класс отправленного сообщения
 * @property message текст сообщения типа [String]
 * @property senderPublicId идентификатор отправителя типа [String]
 * @property attachments список прикрепленных файлов типа [ArrayList]
 */
data class SendMessageDataClass(
    var message: String,
    var senderPublicId: String,
    var attachments: ArrayList<String>?
) {

    /**
     * Функция создания json-объекта отправленного сообщения
     * @return json-объект отправленного сообщения типа [JSONObject]
     */
    fun toJson() = JSONObject().apply {
        put("msg", message)
        put("msg_sender", senderPublicId)
        put("attachments", attachments?.joinToString(";"))
    }.toString()
}
