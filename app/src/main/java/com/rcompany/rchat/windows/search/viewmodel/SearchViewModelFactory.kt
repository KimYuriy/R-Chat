package com.rcompany.rchat.windows.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабрика для создания SearchViewModel
 * @property userRepo репозиторий данных пользователей типа [UserRepo]
 */
@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val chatsRepo: ChatsRepo, private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = SearchViewModel(chatsRepo, userRepo) as T
}