package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import com.rcompany.rchat.utils.databases.chats.dataclasses.common.SenderDataClass
import org.json.JSONObject

data class ReplyToMessageDataClass(
    var id: String,
    var type: String,
    var message_text: String? = null,
    var sender: SenderDataClass
) {
    companion object {
        fun fromJson(json: JSONObject): ReplyToMessageDataClass {
            return ReplyToMessageDataClass(
                json.getString("id"),
                json.getString("type"),
                json.optString("message_text"),
                SenderDataClass.fromJson(json.getJSONObject("sender"))
            )
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("type", type)
        json.put("message_text", message_text)
        json.put("sender", sender.toJson())
        return json
    }
}