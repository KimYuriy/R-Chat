package com.rcompany.rchat.utils.databases.chats

/**
 * Дата-класс данных сообщения
 * @property messageId ID сообщения типа [Int]
 * @property chatId ID чата типа [Int]
 * @property messageSenderId ID отправителя типа [String]
 * @property message текст сообщения типа [String]
 * @property time время сообщения типа [String]
 * @property attachments список прикрепленных файлов типа null-[ArrayList]
 */
data class ReceivedMessageDataClass(
    var messageId: Int,
    var chatId: Int,
    var messageSenderId: String,
    var message: String,
    var time: String,
    var attachments: ArrayList<String>?
)
