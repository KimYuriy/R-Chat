package com.rcompany.rchat.utils.databases.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.ChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedDeleteMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedEditMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedNewMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedReadMessageDataClass
import com.rcompany.rchat.utils.extensions.time2HumanReadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * База данных мессенджера - чаты и сообщения
 */
class ChatsDB private constructor() {
    companion object {
        /**
         * Экземпляр БД типа null-[ChatsDB]
         */
        @Volatile
        private var instance: ChatsDB? = null

        /**
         * Функция получения экземпляра БД. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @return экземпляр БД [instance] типа [ChatsDB]
         */
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ChatsDB().also {
                    instance = it
            }
        }
    }

    private val _chats = ArrayList<ChatDataClass>()
    private val _chatsLiveData = MutableLiveData<ArrayList<ChatDataClass>>()
    private val _allMessages = HashMap<String, ArrayList<MessageDataClass>>()
    private val _messagesLiveData = MutableLiveData<ArrayList<MessageDataClass>>()

    /**
     * Функция добавления чата в массив чатов
     */
    fun addChat(chat: ChatDataClass) {

    }

    /**
     * Функция добавления сообщения в массив сообщений
     */
    fun addMessage(newMessage: ReceivedNewMessageDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesByChatId = _allMessages.getOrPut(newMessage.chat_id) { ArrayList() }
            messagesByChatId.add(MessageDataClass.fromNewMessage(newMessage))
            withContext(Dispatchers.Main) {
                _messagesLiveData.value = messagesByChatId
            }
        }
    }

    fun editMessage(editedMessage: ReceivedEditMessageDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesByChatId = _allMessages[editedMessage.chat_id]
            val index = messagesByChatId?.indexOfFirst { it.id == editedMessage.id  }
            if (index != null) {
                val message = messagesByChatId[index]
                messagesByChatId[index] = message.copyWith {
                    edited_at = time2HumanReadable(editedMessage.edited_at)
                }
                withContext(Dispatchers.Main) {
                    _messagesLiveData.value = messagesByChatId
                }
            }
        }
    }

    fun deleteMessage(deletedMessage: ReceivedDeleteMessageDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesByChatId = _allMessages[deletedMessage.chat_id]
            val index = messagesByChatId?.indexOfFirst { it.id == deletedMessage.id  }
            if (index != null) {
                messagesByChatId.removeAt(index)
                withContext(Dispatchers.Main) {
                    _messagesLiveData.value = messagesByChatId
                }
            }
        }
    }

    fun readMessage(readMessage: ReceivedReadMessageDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesByChatId = _allMessages[readMessage.chat_id]
            val index = messagesByChatId?.indexOfFirst { it.id == readMessage.id  }
            if (index != null) {
                val message = messagesByChatId[index]
                messagesByChatId[index] = message.copyWith {
                    read_by = readMessage.read_by
                }
                withContext(Dispatchers.Main) {
                    _messagesLiveData.value = messagesByChatId
                }
            }
        }
    }

    /**
     * Функция получения массива чатов
     */
    fun getChats() = _chatsLiveData as LiveData<ArrayList<ChatDataClass>>

    /**
     * Функция получения массива сообщений
     */
    fun getMessages() = _messagesLiveData as LiveData<ArrayList<MessageDataClass>>

    fun filterMessagesByChatId(chatId: String) {
        _messagesLiveData.value = _allMessages[chatId] ?: ArrayList()
    }

    fun clearMessagesLiveData() {
        _messagesLiveData.value = ArrayList()
    }

    fun clearChats() {
        _chats.clear()
        _chatsLiveData.value = _chats
    }

}