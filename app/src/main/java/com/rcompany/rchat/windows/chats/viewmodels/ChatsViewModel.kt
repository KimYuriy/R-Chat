package com.rcompany.rchat.windows.chats.viewmodels

import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.chats.ChatsRepo

/**
 * Класс-контроллер состоянием ChatsWindow типа [ViewModel]
 * @property chatsRepo репозиторий БД пользователя типа [ChatsRepo]
 */
class ChatsViewModel(private val chatsRepo: ChatsRepo): ViewModel() {
}