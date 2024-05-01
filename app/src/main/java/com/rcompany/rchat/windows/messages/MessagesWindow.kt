package com.rcompany.rchat.windows.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.R
import com.rcompany.rchat.databinding.MessagesWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.CurrentChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.windows.create_group_chat.adapters.SelectedUserAdapter
import com.rcompany.rchat.windows.create_group_chat.interfaces.CreateGroupChatInterface
import com.rcompany.rchat.windows.messages.adapter.MessageItemAdapter
import com.rcompany.rchat.windows.messages.dataclasses.DataForMessagesWindowDataClass
import com.rcompany.rchat.windows.messages.interfaces.MessageItemInterface
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModel
import com.rcompany.rchat.windows.messages.viewmodel.MessagesViewModelFactory
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class MessageType {
    TEXT, VOICE, VIDEO
}

class MessagesWindow : AppCompatActivity(), CreateGroupChatInterface, MessageItemInterface {
    private lateinit var b: MessagesWindowBinding
    private lateinit var vm: MessagesViewModel
    private var currentChatData: CurrentChatDataClass? = null
    private var msgType = MessageType.VOICE
    private lateinit var handler: Handler
    private lateinit var timeCheckRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MessagesWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val data = intent.getParcelableExtra<DataForMessagesWindowDataClass>("chat_data")

        currentChatData = if (data?.chat_id != null) {
            CurrentChatDataClass(
                id = data.chat_id,
                type = data.chat_type,
                name = data.chat_name,
                avatar_photo_url = data.chat_avatar,
                allow_messages_from = data.allow_messages_from,
                allow_messages_to = data.allow_messages_to
            )
        } else null

        b.tvLogin.text = if (data?.chat_name != null) data.chat_name else data?.public_id

        val userRepo = UserRepo.getInstance(UserDB.getInstance(applicationContext))
        val factory = MessagesViewModelFactory(
            currentChatData,
            data?.public_id,
            ChatsRepo.getInstance(
                ChatsDB.getInstance(),
                NetworkManager(applicationContext, userRepo)),
            userRepo
        )
        vm = ViewModelProvider(this, factory)[MessagesViewModel::class.java]

//        if (data?.allow_messages_from != "null") {
//            b.etMessage.isEnabled = vm.isCurrentTimeBetween(data?.allow_messages_from!!, data.allow_messages_to!!)
//        }

        /**
         * Адаптер для отображения сообщений
         */
        val adapter = MessageItemAdapter(
            ArrayList(),
            vm.getUserData()!!.userId
        )
        b.rvMessages.layoutManager = LinearLayoutManager(this)
        b.rvMessages.adapter = adapter


        val foundUsersAdapter = SelectedUserAdapter(ArrayList(), this)
        val currentUsersAdapter = SelectedUserAdapter(ArrayList(), this)

        b.rvCurrentUsers.layoutManager = LinearLayoutManager(this)
        b.rvCurrentUsers.adapter = currentUsersAdapter

        b.rvFoundUsers.layoutManager = LinearLayoutManager(this)
        b.rvFoundUsers.adapter = foundUsersAdapter

        /**
         * Отслеживание изменения массива сообщений
         */
        vm.messages.observe(this) {
            adapter.updateMessages(it)
            if (it.isNotEmpty()) {
                b.rvMessages.scrollToPosition(it.size - 1)
            }
        }

        vm.foundUsersLiveData.observe(this) {
            foundUsersAdapter.updateArray(it)
        }

        vm.currentUsersLiveData.observe(this) {
            currentUsersAdapter.updateArray(it)
        }

        b.tvLogin.setOnClickListener {
            b.messagesWindow.visibility = View.GONE
            b.chatInfo.visibility = View.VISIBLE
            Log.w("MessagesWindow:tvLogin", data.toString())
            if (data!!.chat_type == ChatTypes.GROUP.value) {
                vm.getCurrentUsers(this@MessagesWindow, data.chat_id!!)
            }
        }

        b.ibBackToMessages.setOnClickListener {
            b.chatInfo.visibility = View.GONE
            b.messagesWindow.visibility = View.VISIBLE
        }

        b.ibSearch.setOnClickListener {
            val input = b.etUserLogin.text.toString()
            vm.searchUser(this@MessagesWindow, input)
        }

        b.btnSend.setOnClickListener {
            when (msgType) {
                MessageType.TEXT -> {
                    vm.sendMessage(b.etMessage.text.toString(), data!!.chat_type)
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

        b.ibSettings.setOnClickListener {
            when (data!!.chat_type) {
                ChatTypes.PRIVATE.value -> {
                    vm.showPrivateChatPopupMenu(this@MessagesWindow, it)
                }
                ChatTypes.GROUP.value -> {
                    vm.showGroupChatPopupMenu(this@MessagesWindow, it)
                }
                else -> {}
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

//        timeCheckRunnable = object : Runnable {
//            override fun run() {
//                checkTimeAndSendSignal()
//                handler.postDelayed(this, 60_000)
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
//        handler.post(timeCheckRunnable)
    }

    override fun onStop() {
        super.onStop()
//        handler.removeCallbacks(timeCheckRunnable)
    }

    private fun checkTimeAndSendSignal() {
        val now = LocalTime.now()
        val startTime = parseTimeString(currentChatData?.allow_messages_from!!)
        val endTime = parseTimeString(currentChatData?.allow_messages_to!!)
        if (now.hour == startTime.hour && now.minute == startTime.minute) {
            b.etMessage.isEnabled = true
        } else if (now.hour == endTime.hour && now.minute == endTime.minute) {
            b.etMessage.isEnabled = false
        }
    }

    private fun parseTimeString(timeString: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return LocalTime.parse(timeString, formatter)
    }

    override fun onUserSelected(id: String) {
        vm.addUserToChat(this@MessagesWindow, currentChatData?.id!!, id)
    }

    override fun onUserUnselected(id: String) {
        vm.removeUserFromChat(this@MessagesWindow, currentChatData?.id!!, id)
    }

    override fun onLongClicked(message: MessageDataClass) {
        vm.showMessageAction(this@MessagesWindow, message)
    }
}