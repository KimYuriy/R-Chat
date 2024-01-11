package com.rcompany.rchat.windows.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.ChatsWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.windows.chats.viewmodels.ChatsViewModel
import com.rcompany.rchat.windows.chats.viewmodels.ChatsViewModelFactory

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

        vm = ViewModelProvider(this, ChatsViewModelFactory(ChatsRepo.getInstance(ChatsDB.getInstance())))[ChatsViewModel::class.java]
    }
}