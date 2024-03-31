package com.rcompany.rchat.utils.databases.chats

import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedDeleteMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedEditMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedNewMessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.ReceivedReadMessageDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Репозиторий для БД чатов и сообщений
 * @property chatsDB база данных чатов и сообщений типа [ChatsDB]
 */
class ChatsRepo private constructor(private val chatsDB: ChatsDB, private val networkManager: NetworkManager) {
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
        fun getInstance(chatsDB: ChatsDB, networkManager: NetworkManager) =
            instance ?: synchronized(this) {
                instance ?: ChatsRepo(chatsDB, networkManager).also {
                    instance = it
                }
            }
    }

    fun getChatsLiveData() = chatsDB.getChats()

    fun getMessages() = chatsDB.getMessages()

    fun filterMessagesByChatId(chatId: String) = chatsDB.filterMessagesByChatId(chatId)

    fun clearMessagesLiveData() = chatsDB.clearMessagesLiveData()

    fun closeConnection() = networkManager.closeConnection()

    private fun responseParseMessage(data: JSONObject) {
        chatsDB.addMessage(ReceivedNewMessageDataClass.fromJson(data))
    }

    private fun responseEditMessage(data: JSONObject) {
        chatsDB.editMessage(ReceivedEditMessageDataClass.fromJson(data))
    }

    private fun responseDeleteMessage(data: JSONObject) {
        chatsDB.deleteMessage(ReceivedDeleteMessageDataClass.fromJson(data))
    }

    private fun responseReadMessage(data: JSONObject) {
        chatsDB.readMessage(ReceivedReadMessageDataClass.fromJson(data))
    }

    fun sendMessage(message: JSONObject) = networkManager.sendMessage(message)

    fun editMessage(message: JSONObject) = networkManager.editMessage(message)

    fun deleteMessage(message: JSONObject) = networkManager.deleteMessage(message)

    fun readMessage(message: JSONObject) = networkManager.readMessage(message)

    init {
        networkManager.openConnection()
        networkManager.listenEvents(
            ::responseParseMessage,
            ::responseEditMessage,
            ::responseDeleteMessage,
            ::responseReadMessage
        )
    }
}