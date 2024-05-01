package com.rcompany.rchat.windows.create_group_chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcompany.rchat.databinding.CreateGroupChatWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.create_group_chat.adapters.SelectedUserAdapter
import com.rcompany.rchat.windows.create_group_chat.interfaces.CreateGroupChatInterface
import com.rcompany.rchat.windows.create_group_chat.viewmodel.CreateGroupChatViewModel
import com.rcompany.rchat.windows.create_group_chat.viewmodel.CreateGroupChatViewModelFactory

class CreateGroupChatWindow : AppCompatActivity(), CreateGroupChatInterface {
    private lateinit var b: CreateGroupChatWindowBinding
    private lateinit var vm: CreateGroupChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = CreateGroupChatWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = CreateGroupChatViewModelFactory(UserRepo.getInstance(UserDB.getInstance(applicationContext)))
        vm = ViewModelProvider(this, factory)[CreateGroupChatViewModel::class.java]

        val foundUsersAdapter = SelectedUserAdapter(ArrayList(), this)
        b.rvFoundUsers.layoutManager = LinearLayoutManager(this)
        b.rvFoundUsers.adapter = foundUsersAdapter

        val selectedUsersAdapter = SelectedUserAdapter(ArrayList(), this)
        b.rvSelectedUsers.layoutManager = LinearLayoutManager(this)
        b.rvSelectedUsers.adapter = selectedUsersAdapter

        vm.foundUsersLiveData.observe(this) {
            foundUsersAdapter.updateArray(it)
        }

        vm.selectedUsersLiveData.observe(this) {
            selectedUsersAdapter.updateArray(it)
            b.btnCreateGroupChat.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
        }

        b.ibSearchPerson.setOnClickListener {
            if (b.etLogin.text.isNotEmpty()) {
                vm.searchUser(this@CreateGroupChatWindow, b.etLogin.text.toString())
            }
        }

        b.ibBack.setOnClickListener { finish() }

        b.btnCreateGroupChat.setOnClickListener {
            vm.showAlertDialog(this@CreateGroupChatWindow)
        }
    }

    override fun onUserSelected(id: String) {
        vm.addSelectedUser(id)
    }

    override fun onUserUnselected(id: String) {
        vm.removeSelectedUser(id)
    }
}