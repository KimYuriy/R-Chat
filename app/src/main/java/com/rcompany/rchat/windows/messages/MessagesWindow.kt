package com.rcompany.rchat.windows.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.MessagesWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.windows.messages.viewmodels.MessagesViewModel
import com.rcompany.rchat.windows.messages.viewmodels.MessagesViewModelFactory

class MessagesWindow : AppCompatActivity() {
    private lateinit var b: MessagesWindowBinding
    private lateinit var vm: MessagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MessagesWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        vm = ViewModelProvider(this, MessagesViewModelFactory(ChatsRepo.getInstance(ChatsDB.getInstance())))[MessagesViewModel::class.java]
    }
}