package com.rcompany.rchat.windows.messages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing.NewMessageDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Класс-контроллер управления состоянием MessagesWindow типа [ViewModel]
 * @property chatsRepo репозиторий чатов типа [ChatsRepo]
 * @property userRepo репозиторий пользователей типа [UserRepo]
 */
class MessagesViewModel(
    private val currentChatId: String,
    private val chatsRepo: ChatsRepo,
    private val userRepo: UserRepo,
): ViewModel() {

    init {
        Log.d("MessagesViewModel:init", "init")
        chatsRepo.filterMessagesByChatId(currentChatId)
    }

    val messages = chatsRepo.getMessages()

    /**
     * Функция получения данных пользователя
     * @return данные пользователя типа
     */
    fun getUserData() = userRepo.getUserData()

    fun sendMessage(message: String) {
        val messageData = NewMessageDataClass(
            chat_id = currentChatId,
            message_text = message,
            sender_user_id = userRepo.getUserData()!!.publicId
        ).toJson()
        chatsRepo.sendMessage(messageData)
    }

    override fun onCleared() {
        chatsRepo.clearMessagesLiveData()
        Log.w("MessagesViewModel:onCleared", "onCleared")
        super.onCleared()
    }
}