package com.rcompany.rchat.windows.chats.viewmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.chats.ChatDataClass
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.socket.Websocket
import com.rcompany.rchat.windows.search.SearchUsersWindow

/**
 * Класс-контроллер состоянием ChatsWindow типа [ViewModel]
 * @property chatsRepo репозиторий БД пользователя типа [ChatsRepo]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class ChatsViewModel(private val chatsRepo: ChatsRepo, private val userRepo: UserRepo): ViewModel() {

    val chats = chatsRepo.getChatsLiveData()

    /**
     * Функция открытия окна поиска пользователей
     */
    fun openSearchWindow(from: AppCompatActivity) {
        from.startActivity(Intent(from, SearchUsersWindow::class.java))
    }

    /**
     * Функция получения пользовательских данных
     * @return данные пользователя типа
     */
    fun getUserData() = userRepo.getUserData()
}