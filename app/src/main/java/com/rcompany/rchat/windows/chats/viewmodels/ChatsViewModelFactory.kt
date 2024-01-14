package com.rcompany.rchat.windows.chats.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабричный класс для ViewModel [ChatsViewModel]
 * @property chatsRepo репозиторий БД пользователя типа [ChatsRepo]
 */
@Suppress("UNCHECKED_CAST")
class ChatsViewModelFactory(private val chatsRepo: ChatsRepo, private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = ChatsViewModel(chatsRepo, userRepo) as T
}