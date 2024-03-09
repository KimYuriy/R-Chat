package com.rcompany.rchat.windows.settings.additional_user_info.viewmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.chats.ChatsWindow

/**
 * Класс-контроллер для добавления информации о пользователе
 * @property userRepo репозиторий данных пользователей типа [UserRepo]
 */
class AdditionalUserInfoViewModel(private val userRepo: UserRepo): ViewModel() {

    /**
     * Нажатие кнопки пропуска текущего шага
     * @param from контекст типа [AppCompatActivity]
     */
    fun onSkipClicked(from: AppCompatActivity) {
        from.startActivity(Intent(from, ChatsWindow::class.java))
        from.finish()
    }

    /**
     * Нажатие кнопки сохранения дополнительных данных о пользователе
     * @param from контекст типа [AppCompatActivity]
     */
    fun onSaveDataClicked(from: AppCompatActivity) {

    }
}