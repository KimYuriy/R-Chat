package com.rcompany.rchat.windows.chats.viewmodel

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.search.SearchUsersWindow

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
                    Toast.makeText(context, context.getString(R.string.wip_text), Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        menu.show()
    }
    
    fun openSearchWindow(from: AppCompatActivity) {
        from.startActivity(Intent(from, SearchUsersWindow::class.java))
    }

    fun getUserData() = userRepo.getUserData()
}