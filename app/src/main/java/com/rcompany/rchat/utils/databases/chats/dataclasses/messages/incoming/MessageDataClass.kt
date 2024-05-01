package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import com.rcompany.rchat.utils.databases.chats.dataclasses.common.SenderDataClass
import org.json.JSONObject

data class MessageDataClass(
    var id: String,
    var type: String,
    var chat: CurrentChatDataClass,
    var message_text: String? = null,
    var audio_file_link: String? = null,
    var video_file_link: String? = null,
    var created_at: String,
    var last_edited_at: String? = null,
    var read_by_users: ArrayList<String> = ArrayList(),
    var reply_to_message: ReplyToMessageDataClass?,
    var forwarded_message: ForwardedMessageDataClass?,
    var sender: SenderDataClass,
    var is_silent: Boolean? = null
) {
    companion object {
        fun fromJson(json: JSONObject, currentChatData: CurrentChatDataClass? = null): MessageDataClass {
            val readByUsers = ArrayList<String>()
            val jsonArray = json.optJSONArray("read_by_users")
            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    readByUsers.add(jsonArray.getString(i))
                }
            }
            return MessageDataClass(
                json.getString("id"),
                json.getString("type"),
                currentChatData ?: CurrentChatDataClass.fromJson(json.getJSONObject("chat")),
                json.optString("message_text"),
                json.optString("audio_file_link"),
                json.optString("video_file_link"),
                json.getString("created_at"),
                json.optString("last_edited_at"),
                readByUsers,
                json.optJSONObject("reply_to_message")?.let { ReplyToMessageDataClass.fromJson(it) },
                json.optJSONObject("forwarded_message")?.let { ForwardedMessageDataClass.fromJson(it) },
                SenderDataClass.fromJson(json.getJSONObject("sender")),
                json.optBoolean("is_silent")
            )
        }
    }
}
