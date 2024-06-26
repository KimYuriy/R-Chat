package com.rcompany.rchat.windows.messages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.CurrentChatDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабричный класс для ViewModel [MessagesViewModel]
 * @property chatsRepo репозиторий чатов и сообщений типа [ChatsRepo]
 * @property userRepo репозиторий данных пользователя типа [UserRepo]
 */
@Suppress("UNCHECKED_CAST")
class MessagesViewModelFactory(
    private val currentChatData: CurrentChatDataClass?,
    private val otherUserPublicID: String?,
    private val chatsRepo: ChatsRepo,
    private val userRepo: UserRepo
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = MessagesViewModel(
        currentChatData,
        otherUserPublicID,
        chatsRepo,
        userRepo
    ) as T
}