package com.rcompany.rchat.utils.databases.chats.enums

enum class MessageTypes {
    CREATED_CHAT {
        override val value = "created_chat"
    },
    USER_REMOVED {
        override  val value = "user_removed"
    };

    abstract val value: String
}