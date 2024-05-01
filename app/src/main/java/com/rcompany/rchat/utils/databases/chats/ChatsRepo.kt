package com.rcompany.rchat.utils.databases.chats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rcompany.rchat.utils.JasonStatham
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.AddedInChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.CurrentChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.utils.notifications.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Репозиторий для БД чатов и сообщений
 * @property chatsDB база данных чатов и сообщений типа [ChatsDB]
 */
class ChatsRepo private constructor(
    private val chatsDB: ChatsDB,
    private val networkManager: NetworkManager
) {
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

    private var _currentChatData: CurrentChatDataClass? = null
    private val notificationManager = NotificationManager(networkManager.context)
    val selectedUsersForGroupChat = MutableLiveData<ArrayList<String>>()

    fun requestForAllChats() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (val state = networkManager.get(ServerEndpoints.GET_CHATS.toString())) {
                    is ResponseState.Success -> {
                        parseChats(state.data)
                    }
                    is ResponseState.Failure -> {
                        Log.w("ChatsRepo:requestForAllChats", state.errorText)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                Log.e("ChatsRepo:requestForAllChats", e.message.toString())
            }
        }
    }

    private fun parseChats(data: JSONObject) {
        Log.w("ChatsRepo:parseChats", data.toString(2))
        val chats = arrayListOf<ChatDataClass>()
        val chatsList = JasonStatham.string2ListJSONs(data.getString("chat_list"))
        for (chat in chatsList) {
            val newChat = ChatDataClass.fromJson(chat)
            chats.add(newChat)
        }
        chatsDB.addAllChats(chats)
    }

    // Срабатывает, когда мы находимся в чате
    fun requestForAllMessages() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val state = networkManager.get(
                    ServerEndpoints.GET_MESSAGES.toString(),
                    mapOf(
                        "chat_id" to _currentChatData!!.id!!,
                        "limit" to "${Int.MAX_VALUE}"
                    )
                )
                when (state) {
                    is ResponseState.Success -> {
                        parseMessages(state.data)
                    }
                    is ResponseState.Failure -> {
                        Log.d("ChatsRepo:requestForAllMessages", state.errorText)
                    }
                    else -> {}
                }
            }
        } catch (e: Exception) {
            Log.d("ChatsRepo:requestForAllMessages", e.message.toString())
        }
    }

    // Срабатывает, когда мы находимся в чате
    private fun parseMessages(data: JSONObject) {
        Log.w("ChatsRepo:parseMessages", data.toString(2))
        val messages = arrayListOf<MessageDataClass>()
        val messagesList = JasonStatham.string2ListJSONs(data.getString("messages"))
        for (message in messagesList) {
            val newMessage = MessageDataClass.fromJson(message, _currentChatData)
            messages.add(newMessage)
        }
        chatsDB.addAllMessages(messages, _currentChatData!!.id!!)
    }

    fun getChatsLiveData() = chatsDB.getChats()

    fun getMessages() = chatsDB.getMessages()

    // Срабатывает, когда мы заходим в чат
    fun filterMessagesByChatData(currentChatData: CurrentChatDataClass) {
        _currentChatData = currentChatData
        chatsDB.filterMessagesByChatId(_currentChatData!!.id!!)
    }

    // Срабатывает, когда мы выходим из чата
    fun clearMessagesLiveData() {
        _currentChatData = null
        chatsDB.clearMessagesLiveData()
    }

    fun closeConnection() = networkManager.closeConnection()

    /**
     * Срабатывает при получении нового сообщения в независимости от того, находимся мы в чате или нет
      */
    private fun responseParseMessage(data: JSONObject) {
        Log.w("ChatsRepo:responseParseMessage", data.toString(2))
        val newMessage = MessageDataClass.fromJson(data)
        Log.w("ChatsRepo:responseParseMessage", newMessage.toString())
        var shouldBeShown = true
        if (newMessage.chat.id != _currentChatData?.id) {
            shouldBeShown = false
            if (newMessage.sender.user_id == networkManager.userData!!.userId) {
                shouldBeShown = true
            } else {
                notificationManager.showMessageNotification(newMessage)
            }
        }
        chatsDB.addMessage(newMessage, shouldBeShown)
    }

    private fun responseEditMessage(data: JSONObject) {
        chatsDB.editMessage()
    }

    private fun responseDeleteMessage(data: JSONObject) {
        chatsDB.deleteMessage()
    }

    private fun responseReadMessage(data: JSONObject) {
        chatsDB.readMessage()
    }

    private fun responseAddedInChat(data: JSONObject) {
        chatsDB.addedInChat(AddedInChatDataClass.fromJson(data))
    }

    fun sendMessage(message: JSONObject) = networkManager.sendMessage(message)

    fun editMessage(message: JSONObject) = networkManager.editMessage(message)

    fun deleteMessage(message: JSONObject) = networkManager.deleteMessage(message)

    fun readMessage(message: JSONObject) = networkManager.readMessage(message)

    init {
        selectedUsersForGroupChat.value = arrayListOf()
        CoroutineScope(Dispatchers.IO).launch {
            networkManager.openConnection()
            networkManager.listenEvents(
                parseNewMessage = ::responseParseMessage,
                editMessage = ::responseEditMessage,
                deleteMessage = ::responseDeleteMessage,
                readMessage = ::responseReadMessage,
                addedInChat = ::responseAddedInChat
            )
        }
    }
}