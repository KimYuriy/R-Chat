package com.rcompany.rchat.windows.messages.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataForMessagesWindowDataClass(
    var public_id: String? = null,
    var chat_id: String? = null,
    var chat_name: String? = null,
    var chat_type: String? = null,
    var chat_avatar: String? = null,
    var is_work_chat: Boolean? = null,
    var allow_messages_from: String? = null,
    var allow_messages_to: String? = null,
): Parcelable