package com.rcompany.rchat.utils.databases.chats.enums

enum class ChatTypes {
    PRIVATE {
        override val value = "private"
    },
    WORK {
         override val value = "work"
    },
    GROUP {
          override val value = "group"
    },
    CHANNEL {
        override val value = "channel"
    };

    abstract val value: String
}