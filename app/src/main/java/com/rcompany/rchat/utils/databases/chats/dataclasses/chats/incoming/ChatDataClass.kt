package com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming

import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import org.json.JSONObject

/**
 * Дата-класс данных чата

 */
data class ChatDataClass(
    var id: String,
    var name: String,
    var type: String,
    var is_work_chat: Boolean? = null,
    var allow_messages_from: String? = null,
    var allow_messages_to: String? = null,
    var last_message: LastMessageDataClass? = null,
    var avatar_photo_url: String? = null,
    var created_by_id: String? = null,
    var created_by_first_name: String? = null,
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): ChatDataClass {
            val lastMessage = if (jsonObject["last_message"] == JSONObject.NULL) null else LastMessageDataClass.fromJson(jsonObject.getJSONObject("last_message"))
            return ChatDataClass(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                jsonObject.getString("type"),
                jsonObject.optBoolean("is_work_chat"),
                jsonObject.optString("allow_messages_from"),
                jsonObject.optString("allow_messages_to"),
                lastMessage,
                jsonObject.optString("avatar_photo_url"),
                jsonObject.optJSONObject("created_by")?.getString("id"),
                jsonObject.optJSONObject("created_by")?.getString("first_name")
            )
        }

        fun fromMessageDataClass(message: MessageDataClass): ChatDataClass {
            return ChatDataClass(
                id = message.chat.id!!,
                name = message.chat.name!!,
                type = message.chat.type!!,
                avatar_photo_url = message.chat.avatar_photo_url!!,
                last_message = LastMessageDataClass.fromMessageDataClass(message)
            )
        }
    }
}