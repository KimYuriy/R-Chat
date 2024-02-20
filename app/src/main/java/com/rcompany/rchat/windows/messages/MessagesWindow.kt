package com.rcompany.rchat.windows.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.MessagesWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
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
            ChatsRepo.getInstance(ChatsDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[MessagesViewModel::class.java]

        b.tvLogin.text = intent.getStringExtra("public_id")

        b.ibBack.setOnClickListener {
            finish()
        }
    }
}