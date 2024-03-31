package com.rcompany.rchat.windows.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.R
import com.rcompany.rchat.databinding.MessagesWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.windows.messages.adapter.MessageItemAdapter
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModel
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModelFactory

enum class MessageType {
    TEXT, VOICE, VIDEO
}

class MessagesWindow : AppCompatActivity() {
    private lateinit var b: MessagesWindowBinding
    private lateinit var vm: MessagesViewModel
    private var msgType = MessageType.VOICE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MessagesWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        /**
         * Получение public_id собеседника
         */
        val chatId = intent.getStringExtra("public_id")
        b.tvLogin.text = chatId

        val userRepo = UserRepo.getInstance(UserDB.getInstance(applicationContext))
        val factory = MessagesViewModelFactory(
            chatId!!,
            ChatsRepo.getInstance(ChatsDB.getInstance(), NetworkManager(applicationContext, userRepo)),
            userRepo
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
        vm.messages.observe(this) {
            adapter.updateMessages(it)
            if (it.isNotEmpty()) {
                b.rvMessages.scrollToPosition(it.size - 1)
            }
        }

        b.btnSend.setOnClickListener {
            when (msgType) {
                MessageType.TEXT -> {
                    vm.sendMessage(b.etMessage.text.toString())
                    b.etMessage.text.clear()
                }
                MessageType.VOICE -> {
                    b.btnSend.setImageResource(R.drawable.img_videomessage)
                    msgType = MessageType.VIDEO
                    // todo: сделать отправку голосового сообщения
                }
                MessageType.VIDEO -> {
                    b.btnSend.setImageResource(R.drawable.img_microphone)
                    msgType = MessageType.VOICE
                }
            }
        }

        /**
         * Нажатие на кнопку выхода из текущего окна
         */
        b.ibBack.setOnClickListener {
            finish()
        }

        b.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.btnSend.setImageResource(if (s.isNullOrEmpty()) R.drawable.img_microphone else R.drawable.img_send)
                msgType = if (s.isNullOrEmpty()) MessageType.VOICE else MessageType.TEXT
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("Back button pressed")
                finish()
            }
        })
    }
}