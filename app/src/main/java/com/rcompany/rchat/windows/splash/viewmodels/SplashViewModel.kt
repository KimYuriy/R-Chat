package com.rcompany.rchat.windows.splash.viewmodels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.AuthWindow
import java.util.Timer
import kotlin.concurrent.schedule

class SplashViewModel(private val userRepo: UserRepo) : ViewModel() {
    /**
     * Функция открытия окна с задержкой.
     * Принимаемые параметры - текущее окно и время задержки.
     * Устанавливает таймер на время delay и по прошествии времени проверяет, сохранены ли данные
     * пользователя в приложении (т.е. авторизован ли он). Если данные есть - открывает окно с чатами,
     * если нет - окно авторизации
     */
    fun openNextWindowAfterDelay(from: AppCompatActivity, delay: Long) {
        Timer().schedule(delay) {
            if (!userRepo.isUserAuthorized()) {
                userRepo.saveUserData(1, "KimYuriy", "ShooterPhoto")
                from.startActivity(Intent(from, AuthWindow::class.java))
                from.finish()
            }
        }
    }

    /**
     * Функция установки текста приветствия. Проверяет, сохранены ли данные
     * пользователя в приложении (т.е. авторизован ли он). Если сохранены - устанавливает
     * именованное приветствие, если нет - стандартное приветствие
     */
    fun getGreetingText(from: AppCompatActivity) = from.getString(R.string.greeting_default_text)
}