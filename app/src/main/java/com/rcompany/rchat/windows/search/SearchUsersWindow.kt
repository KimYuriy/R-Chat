package com.rcompany.rchat.windows.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.SearchUsersWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
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

        val factory = SearchViewModelFactory(
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        val adapter = FoundUsersAdapter(ArrayList())
        b.rvFoundUsers.layoutManager = LinearLayoutManager(this)
        b.rvFoundUsers.adapter = adapter

        vm.foundUsersLiveData.observe(this) {
            adapter.updateFoundUsers(it)
        }

        b.ibSearchPerson.setOnClickListener {
            vm.searchUser(
                b.etLogin.text.toString(),
                this@SearchUsersWindow
            )
        }
    }
}