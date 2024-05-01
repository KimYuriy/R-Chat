package com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming

import android.util.Log
import com.rcompany.rchat.utils.databases.chats.dataclasses.common.SenderDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import org.json.JSONObject

data class LastMessageDataClass(
    var id: String,
    var message_type: String,
    var created_at: String,
    var sender: SenderDataClass,
    var message_text: String? = null,
) {
    companion object {
        fun fromJson(json: JSONObject): LastMessageDataClass {
            Log.w("LastMessageDataClass:fromJson", "${json == JSONObject.NULL}")
            return LastMessageDataClass(
                json.getString("id"),
                json.getString("message_type"),
                json.getString("created_at"),
                SenderDataClass.fromJson(json.getJSONObject("sender")),
                json.optString("message_text")
            )
        }

        fun fromMessageDataClass(message: MessageDataClass): LastMessageDataClass {
            return LastMessageDataClass(
                id = message.id,
                message_type = message.type,
                created_at = message.created_at,
                message_text = message.message_text,
                sender = message.sender
            )
        }
    }
}