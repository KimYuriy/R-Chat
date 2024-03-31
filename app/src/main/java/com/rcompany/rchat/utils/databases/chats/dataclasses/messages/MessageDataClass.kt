package com.rcompany.rchat.utils.databases.chats.dataclasses.messages

import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedNewMessageDataClass

data class MessageDataClass(
    var id: String,
    var chat_id: String,
    var message_text: String?,
    var sender_user_id: String,
    var created_at: String,
    var reply_to_message: String? = null,
    var forwarded_message: String? = null,
    var is_silent: Boolean = false,
    var edited_at: String? = null,
    var read_by: String? = null,
) {
    companion object {
        fun fromNewMessage(newMessage: ReceivedNewMessageDataClass) = MessageDataClass(
            id = newMessage.id,
            chat_id = newMessage.chat_id,
            message_text = newMessage.message_text,
            sender_user_id = newMessage.sender_user_id,
            reply_to_message = newMessage.reply_to_message,
            created_at = newMessage.created_at,
            forwarded_message = newMessage.forwarded_message,
            is_silent = newMessage.is_silent,
        )
    }

    /**
     * Функция для создания копии объекта с возможностью изменения свойств.
     */
    fun copyWith(block: MessageDataClass.() -> Unit): MessageDataClass {
        return this.apply { block() }
    }
}
