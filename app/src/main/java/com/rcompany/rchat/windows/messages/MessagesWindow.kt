package com.rcompany.rchat.windows.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.MessagesWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.messages.adapter.MessageItemAdapter
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModel
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModelFactory

class MessagesWindow : AppCompatActivity() {
    private lateinit var b: MessagesWindowBinding
    private lateinit var vm: MessagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MessagesWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = MessagesViewModelFactory(
            ChatsRepo.getInstance(ChatsDB.getInstance(applicationContext)),
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[MessagesViewModel::class.java]

        /**
         * Адаптер для отображения сообщений
         */
        val adapter = MessageItemAdapter(
            ArrayList(),
            vm.getUserData()!!.publicId
        )
        b.rvMessages.layoutManager = LinearLayoutManager(this)
        b.rvMessages.adapter = adapter

        /**
         * Отслеживание изменения массива сообщений
         */
        vm.messagesLiveData.observe(this) {
            adapter.updateMessages(it)
        }

        /**
         * Получение public_id собеседника
         */
        b.tvLogin.text = intent.getStringExtra("public_id")

        /**
         * Нажатие на кнопку выхода из текущего окна
         */
        b.ibBack.setOnClickListener {
            finish()
        }
    }
}