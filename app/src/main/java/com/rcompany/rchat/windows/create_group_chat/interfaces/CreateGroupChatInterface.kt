package com.rcompany.rchat.windows.create_group_chat.interfaces

interface CreateGroupChatInterface {
    fun onUserSelected(id: String)

    fun onUserUnselected(id: String)
}