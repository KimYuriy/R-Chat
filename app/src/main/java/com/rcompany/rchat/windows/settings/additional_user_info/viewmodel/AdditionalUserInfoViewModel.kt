package com.rcompany.rchat.windows.settings.additional_user_info.viewmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.chats.ChatsWindow

class AdditionalUserInfoViewModel(private val userRepo: UserRepo): ViewModel() {
    fun onSkipClicked(from: AppCompatActivity) {
        from.startActivity(Intent(from, ChatsWindow::class.java))
        from.finish()
    }

    fun onSaveDataClicked(from: AppCompatActivity) {

    }
}