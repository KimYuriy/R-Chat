package com.rcompany.rchat.utils.databases.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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

    private val _chatsLiveData = MutableLiveData<ArrayList<ChatDataClass>>()
    private val _messagesLiveData = MutableLiveData<ArrayList<MessageDataClass>>()

    fun addChat(chat: ChatDataClass) {
        //TODO: Сделать поиск чата
        _chatsLiveData.value?.add(0, chat)
    }

    fun addChats(chats: ArrayList<ChatDataClass>) {
        _chatsLiveData.value?.clear()
        _chatsLiveData.value?.addAll(chats)
    }

    fun addMessage(newMessage: MessageDataClass) {
        //TODO: Сделать поиск сообщения
        _messagesLiveData.value?.add(newMessage)
    }

    fun addMessages(messages: ArrayList<MessageDataClass>) {
        _messagesLiveData.value?.clear()
        _messagesLiveData.value?.addAll(messages)
    }

    fun getMessages() = _messagesLiveData as LiveData<ArrayList<MessageDataClass>>

    fun getChats() = _chatsLiveData as LiveData<ArrayList<ChatDataClass>>


    init {
        _chatsLiveData.value = arrayListOf()
        _messagesLiveData.value = arrayListOf()
    }
}