package com.rcompany.rchat.windows.messages.interfaces

import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass

interface MessageItemInterface {
    fun onLongClicked(message: MessageDataClass)
}