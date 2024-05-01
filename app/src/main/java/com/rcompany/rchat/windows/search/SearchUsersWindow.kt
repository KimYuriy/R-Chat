package com.rcompany.rchat.windows.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.SearchUsersWindowBinding
import com.rcompany.rchat.utils.databases.chats.ChatsDB
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.windows.search.adapter.FoundUsersAdapter
import com.rcompany.rchat.windows.search.viewmodel.SearchViewModel
import com.rcompany.rchat.windows.search.viewmodel.SearchViewModelFactory

/**
 * Класс окна поиска пользователей
 */
class SearchUsersWindow : AppCompatActivity() {
    private lateinit var b: SearchUsersWindowBinding
    private lateinit var vm: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = SearchUsersWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val userRepo = UserRepo.getInstance(UserDB.getInstance(applicationContext))
        val factory = SearchViewModelFactory(
            ChatsRepo.getInstance(
                ChatsDB.getInstance(),
                NetworkManager(applicationContext, userRepo)
            ),
            userRepo
        )
        vm = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        /**
         * Установка адаптера списка пользователей
         */
        val adapter = FoundUsersAdapter(ArrayList())
        b.rvFoundUsers.layoutManager = LinearLayoutManager(this)
        b.rvFoundUsers.adapter = adapter

        /**
         * Отслеживание изменения списка пользователей
         */
        vm.foundUsersLiveData.observe(this) {
            adapter.updateFoundUsers(it)
        }

        /**
         * Нажатие кнопки поиска пользователей
         */
        b.ibSearchPerson.setOnClickListener {
            vm.searchUser(
                b.etLogin.text.toString(),
                this@SearchUsersWindow
            )
        }

        /**
         * Нажатие кнопки выхода из текущего окна
         */
        b.ibBack.setOnClickListener {
            finish()
        }
    }
}