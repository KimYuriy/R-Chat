package com.rcompany.rchat.utils.databases.chats

/**
 * Дата-класс данных чата
 * @property chatId ID чата типа [Int]
 * @property chatName логин собеседника типа [String]
 * @property chatAvatar строка аватарки типа null-[String]
 * @property lastMessage сообщение типа [String]
 * @property time время/дата получения сообщения типа [String]
 * @property lastMessageSenderId ID чата типа [String], по которому происходит получение данных чата
 */
data class ChatDataClass(
    var chatId: Int,
    var chatName: String,
    var chatAvatar: String?,
    var lastMessage: String,
    var time: String,
    var lastMessageSenderId: String
)