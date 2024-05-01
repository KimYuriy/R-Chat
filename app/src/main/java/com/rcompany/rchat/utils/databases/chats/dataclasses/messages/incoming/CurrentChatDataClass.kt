package com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming

import org.json.JSONObject

data class CurrentChatDataClass(
    var id: String?,
    var type: String?,
    var name: String?,
    var avatar_photo_url: String? = null,
    var created_at: String? = null,
    var is_work_chat: Boolean? = null,
    var allow_messages_from: String? = null,
    var allow_messages_to: String? = null,
    var created_by_id: String? = null,
    var created_by_first_name: String? = null,
) {
    companion object {
        fun fromJson(json: JSONObject): CurrentChatDataClass {
            return CurrentChatDataClass(
                json.optString("id"),
                json.optString("type"),
                json.optString("name"),
                json.optString("avatar_photo_url"),
                json.optString("created_at"),
                json.optBoolean("is_work_chat"),
                json.optString("allow_messages_from"),
                json.optString("allow_messages_to"),
                json.optJSONObject("created_by")?.optString("id"),
                json.optJSONObject("created_by")?.optString("first_name")
            )
        }
    }
}