package com.rcompany.rchat.utils.databases.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.AddedInChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun addAllChats(chats: ArrayList<ChatDataClass>) {
        CoroutineScope(Dispatchers.IO).launch {
            _chats.addAll(chats)
            withContext(Dispatchers.Main) {
                _chatsLiveData.value = _chats
            }
        }
    }

    private fun updateChats(chat: ChatDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingChatIndex = _chats.indexOfFirst { it.id == chat.id }
            if (existingChatIndex != -1) {
                Log.d("chatsDB:updateChats", "Chat existing")
                _chats[existingChatIndex] = _chats[existingChatIndex].copy(
                    last_message = chat.last_message
                )
                val updatedChat = _chats.removeAt(existingChatIndex)
                _chats.add(0, updatedChat)
            } else {
                Log.d("chatsDB:updateChats", "New chat")
                _chats.add(0, chat)
            }
            withContext(Dispatchers.Main){
                _chatsLiveData.value = _chats
            }
        }
    }

    fun addAllMessages(messages: ArrayList<MessageDataClass>, currentChatId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesForChat = _allMessages.getOrPut(currentChatId) { ArrayList() }
            if (messagesForChat.isNotEmpty()) {
                messagesForChat.clear()
            }
            messagesForChat.addAll(messages)
            withContext(Dispatchers.Main) {
                _messagesLiveData.value = messagesForChat
            }
        }
    }

    /**
     * Функция добавления сообщения в массив сообщений
     */
    fun addMessage(message: MessageDataClass, shouldBeShown: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val messagesForChat = _allMessages.getOrPut(message.chat.id!!) { ArrayList() }
            messagesForChat.add(message)
            updateChats(ChatDataClass.fromMessageDataClass(message))
            withContext(Dispatchers.Main) {
                if (shouldBeShown) {
                    _messagesLiveData.value = messagesForChat
                }
            }
        }
    }

    fun editMessage() {

    }

    fun deleteMessage() {

    }

    fun readMessage() {

    }

    fun addedInChat(data: AddedInChatDataClass) {
        val id = data.chat_id
        val name = data.name
        val type = if (data.is_work_chat) ChatTypes.WORK.value else ChatTypes.GROUP.value
        val avatarPhotoUrl = data.avatar_photo_url
        val chat = ChatDataClass(id, name, type, null, avatarPhotoUrl)
        _chats.add(0, chat)
        _chatsLiveData.value = _chats
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