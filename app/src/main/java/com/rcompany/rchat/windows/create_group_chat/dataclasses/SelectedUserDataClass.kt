package com.rcompany.rchat.windows.create_group_chat.dataclasses

data class SelectedUserDataClass(
    var publicId: String? = null,
    var userId: String? = null,
    var avatar_photo_url: String? = null,
    var is_selected: Boolean? = null,
    var name: String? = null,
    var chat_role: String? = null,
    var last_online: String? = null,
    var can_exclude: Boolean? = null
)
