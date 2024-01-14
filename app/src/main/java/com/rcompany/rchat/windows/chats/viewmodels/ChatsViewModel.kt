package com.rcompany.rchat.windows.chats.viewmodels

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Класс-контроллер состоянием ChatsWindow типа [ViewModel]
 * @property chatsRepo репозиторий БД пользователя типа [ChatsRepo]
 */
class ChatsViewModel(private val chatsRepo: ChatsRepo, private val userRepo: UserRepo): ViewModel() {
    fun showChatsMenu(context: Context, view: View) {
        val menu = PopupMenu(context, view)
        menu.inflate(R.menu.create_chats_menu)
        menu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.newChat -> {
                    true
                }
                R.id.newGroupChat -> {
                    Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        menu.show()
    }

    fun getUserData() = userRepo.getUserData()
}