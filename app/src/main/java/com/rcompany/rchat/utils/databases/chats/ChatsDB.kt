package com.rcompany.rchat.utils.databases.chats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * База данных мессенджера - чаты и сообщения
 */
class ChatsDB private constructor(private val applicationContext: Context) {
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
        fun getInstance(applicationContext: Context) =
            instance ?: synchronized(this) {
                instance ?: ChatsDB(applicationContext).also {
                    instance = it
            }
        }
    }

    private val _chatsLiveData = MutableLiveData<ArrayList<ChatDataClass>>()
    private val _messagesLiveData = MutableLiveData<ArrayList<MessageDataClass>>()

    /**
     * Функция добавления чата в массив чатов
     */
    fun addChat(chat: ChatDataClass) {
        //TODO: Сделать поиск чата
        _chatsLiveData.value?.add(0, chat)
    }

    /**
     * Функция добавления всех чатов в массив чатов
     */
    fun addChats(chats: ArrayList<ChatDataClass>) {
        _chatsLiveData.value?.clear()
        _chatsLiveData.value?.addAll(chats)
    }

    /**
     * Функция добавления сообщения в массив сообщений
     */
    fun addMessage(newMessage: MessageDataClass) {
        //TODO: Сделать поиск сообщения
        _messagesLiveData.value?.add(newMessage)
    }

    /**
     * Функция добавления сообщений в массив сообщений
     */
    fun addMessages(messages: ArrayList<MessageDataClass>) {
        _messagesLiveData.value?.clear()
        _messagesLiveData.value?.addAll(messages)
    }

    /**
     * Функция получения массива сообщений
     */
    fun getMessages() = _messagesLiveData as LiveData<ArrayList<MessageDataClass>>

    /**
     * Функция получения массива чатов
     */
    fun getChats() = _chatsLiveData as LiveData<ArrayList<ChatDataClass>>


    init {
        _chatsLiveData.value = arrayListOf()
        _messagesLiveData.value = arrayListOf()
    }
}