package com.rcompany.rchat.windows.messages.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.ReceivedMessageDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Класс-контроллер управления состоянием MessagesWindow типа [ViewModel]
 * @property chatsRepo репозиторий чатов типа [ChatsRepo]
 * @property userRepo репозиторий пользователей типа [UserRepo]
 */
class MessagesViewModel(
    private val chatsRepo: ChatsRepo,
    private val userRepo: UserRepo
): ViewModel() {

    /**
     * Массив с сообщениями типа [ArrayList]
     */
    private val _messagesList = ArrayList<ReceivedMessageDataClass>()

    /**
     * Массив с сообщениями типа, доступный извне [MutableLiveData]
     */
    val messagesLiveData = MutableLiveData<ArrayList<ReceivedMessageDataClass>>()

    /**
     * Функция получения данных пользователя
     * @return данные пользователя типа
     */
    fun getUserData() = userRepo.getUserData()
}