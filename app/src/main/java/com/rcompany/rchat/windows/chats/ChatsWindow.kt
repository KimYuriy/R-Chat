package com.rcompany.rchat.windows.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.ChatsWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.windows.chats.adapter.ChatItemAdapter
import com.rcompany.rchat.windows.chats.interfaces.ChatItemInterface
import com.rcompany.rchat.windows.chats.viewmodel.ChatsViewModel
import com.rcompany.rchat.windows.chats.viewmodel.ChatsViewModelFactory

/**
 * Класс окна чатов типа [AppCompatActivity]
 */
class ChatsWindow : AppCompatActivity(), ChatItemInterface {
    private lateinit var b: ChatsWindowBinding
    private lateinit var vm: ChatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ChatsWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val userRepository = UserRepo.getInstance(UserDB.getInstance(applicationContext))
        val factory = ChatsViewModelFactory(
            ChatsRepo.getInstance(ChatsDB.getInstance(), NetworkManager(applicationContext, userRepository)),
            userRepository
        )
        vm = ViewModelProvider(this, factory)[ChatsViewModel::class.java]

        /**
         * Установка адаптера списка чатов
         */
        val adapter = ChatItemAdapter(
            ArrayList(),
            vm.getUserMetaData()!!.userId,
            this
        )
        b.rvChats.layoutManager = LinearLayoutManager(this)
        b.rvChats.adapter = adapter

        /**
         * Отслеживание изменений в списке чатов
         */
        vm.chats.observe(this) {
            adapter.updateChats(it)
        }

        /**
         * Нажатие кнопки поиска пользователя
         */
        b.ibNewChat.setOnClickListener {
            vm.showPopupMenu(this@ChatsWindow, it)
        }

        /**
         * Установка имени текущего пользователя
         */
        b.tvLogin.text = vm.userData?.first_name
    }

    override fun onLongClicked(chat: ChatDataClass) {
        vm.showBottomSheetDialog(this@ChatsWindow, chat)
    }
}