package com.rcompany.rchat.utils.databases.chats

/**
 * Дата-класс данных чата
 * @property id ID чата типа [Int]
 * @property login логин собеседника типа [String]
 * @property avatar строка аватарки типа null-[String]
 * @property message сообщение типа [String]
 * @property time время/дата получения сообщения типа [String]
 * @property lastMessageSenderId ID чата типа [String], по которому происходит получение данных чата
 */
data class ChatDataClass(
    var id: Int,
    var login: String,
    var avatar: String?,
    var message: String,
    var time: String,
    var lastMessageSenderId: Int
)