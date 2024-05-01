package com.rcompany.rchat.windows.chats.interfaces

import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass

interface ChatItemInterface {
    fun onLongClicked(chat: ChatDataClass)
}