package com.rcompany.rchat.windows.messages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.chats.ChatsRepo

@Suppress("UNCHECKED_CAST")
class MessagesViewModelFactory(private val chatsRepo: ChatsRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = MessagesViewModel(chatsRepo) as T
}