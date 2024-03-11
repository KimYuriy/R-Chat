package com.rcompany.rchat.utils.databases.chats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rcompany.rchat.utils.network.socket.Websocket
import org.json.JSONObject

/**
 * Репозиторий для БД чатов и сообщений
 * @property chatsDB база данных чатов и сообщений типа [ChatsDB]
 */
class ChatsRepo private constructor(private val chatsDB: ChatsDB) {
    companion object {

        /**
         * Экземпляр репозитория типа null-[ChatsRepo]
         */
        @Volatile
        private var instance: ChatsRepo? = null

        /**
         * Функция получения экземпляра репозитория. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @param chatsDB база данных чатов и сообщений типа [ChatsDB]
         * @return экземпляр репозитория [instance] типа [ChatsRepo]
         */
        fun getInstance(chatsDB: ChatsDB) =
            instance ?: synchronized(this) {
                instance ?: ChatsRepo(chatsDB).also {
                    instance = it
                }
            }
    }

    private var websocket: Websocket? = null

    /**
     * Приватный список всех чатов пользователя типа [ArrayList]
     */
    private val _chatsList = ArrayList<ChatDataClass>()
    private val chatsLiveData = MutableLiveData<ArrayList<ChatDataClass>>()

    /**
     * Массив с сообщениями типа [ArrayList]
     */
    private val _messagesList = ArrayList<ReceivedMessageDataClass>()
    private val messagesLiveData = MutableLiveData<ArrayList<ReceivedMessageDataClass>>()

    fun getChatsLiveData() = chatsLiveData

    fun getMessagesLiveData() = messagesLiveData

    fun receiveMessage(data: JSONObject) {
        Log.d("ChatsRepo:processMessage", data.toString())
    }

    fun sendMessage(message: String) {
        websocket?.send(message)
    }

    init {
        Log.d("ChatsRepo:init", "init")
        websocket = Websocket.getInstance(this)
    }

}