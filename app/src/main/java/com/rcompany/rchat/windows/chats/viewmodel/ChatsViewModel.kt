package com.rcompany.rchat.windows.chats.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rcompany.rchat.R
import com.rcompany.rchat.databinding.ChatActionsBottomSheetBinding
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.databases.user.dataclasses.UserDataClass
import com.rcompany.rchat.windows.create_group_chat.CreateGroupChatWindow
import com.rcompany.rchat.windows.search.SearchUsersWindow

/**
 * Класс-контроллер состоянием ChatsWindow типа [ViewModel]
 * @property chatsRepo репозиторий БД пользователя типа [ChatsRepo]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class ChatsViewModel(
    private val chatsRepo: ChatsRepo,
    private val userRepo: UserRepo
): ViewModel() {
    init {
        chatsRepo.requestForAllChats()

    }

    val chats = chatsRepo.getChatsLiveData()

    val userData = userRepo.getUserData()

    /**
     * Функция получения пользовательских данных
     * @return данные пользователя типа
     */
    fun getUserMetaData() = userRepo.getUserMetaData()

    fun showPopupMenu(context: Context, v: View) {
        val menu = PopupMenu(context, v)
        menu.menuInflater.inflate(R.menu.create_chats_menu, menu.menu)
        menu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.newChat -> {
                    context.startActivity(Intent(context, SearchUsersWindow::class.java))
                    true
                }
                R.id.newGroupChat -> {
                    context.startActivity(Intent(context, CreateGroupChatWindow::class.java))
                    true
                }
                else -> false
            }
        }
        menu.show()
    }

    fun showBottomSheetDialog(context: Context, chat: ChatDataClass) {
        val binding = ChatActionsBottomSheetBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(binding.root)
        binding.tvChatName.text = chat.name
        binding.btnDeleteChat.setOnClickListener {

        }
        binding.btnMute.setOnClickListener {

        }
        bottomSheetDialog.show()
    }

    override fun onCleared() {
        chatsRepo.closeConnection()
        Log.w("ChatsViewModel:onCleared", "onCleared")
        super.onCleared()
    }
}