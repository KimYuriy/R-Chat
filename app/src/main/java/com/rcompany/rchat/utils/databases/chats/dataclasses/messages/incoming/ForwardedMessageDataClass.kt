package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import com.rcompany.rchat.utils.databases.chats.dataclasses.common.SenderDataClass
import org.json.JSONObject

data class ForwardedMessageDataClass(
    var id: String,
    var type: String,
    var message_text: String? = null,
    var sender: SenderDataClass
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): ForwardedMessageDataClass {
            return ForwardedMessageDataClass(
                jsonObject.getString("id"),
                jsonObject.getString("type"),
                jsonObject.optString("message_text"),
                SenderDataClass.fromJson(jsonObject.getJSONObject("sender"))
            )
        }
    }

    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("type", type)
        jsonObject.put("message_text", message_text)
        jsonObject.put("sender", sender.toJson())
        return jsonObject
    }
}
