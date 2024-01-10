package com.rcompany.rchat.utils.databases.chats

data class ChatDataClass(
    var selfId: Int,
    var login: String,
    var avatar: String?,
    var message: String,
    var time: String,
    var chatId: String
)