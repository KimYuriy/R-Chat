package com.rcompany.rchat.windows.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.ChatsWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.chats.adapter.ChatItemAdapter
import com.rcompany.rchat.windows.chats.viewmodel.ChatsViewModel
import com.rcompany.rchat.windows.chats.viewmodel.ChatsViewModelFactory

/**
 * Класс окна чатов типа [AppCompatActivity]
 */
class ChatsWindow : AppCompatActivity() {
    private lateinit var b: ChatsWindowBinding
    private lateinit var vm: ChatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ChatsWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = ChatsViewModelFactory(
            ChatsRepo.getInstance(ChatsDB.getInstance(applicationContext)),
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[ChatsViewModel::class.java]

        /**
         * Установка адаптера списка чатов
         */
        val adapter = ChatItemAdapter(
            ArrayList(),
            vm.getUserData()!!.publicId,
        )
        b.rvChats.layoutManager = LinearLayoutManager(this)
        b.rvChats.adapter = adapter

        /**
         * Отслеживание изменений в списке чатов
         */
        vm.chatsLiveData.observe(this) {
            adapter.updateChats(it)
        }

        /**
         * Нажатие кнопки поиска пользователя
         */
        b.ibNewChat.setOnClickListener {
            vm.openSearchWindow(this@ChatsWindow)
        }

        /**
         * Установка имени текущего пользователя
         */
        b.tvLogin.text = vm.getUserData()!!.publicId
    }
}