package com.rcompany.rchat.windows.splash.viewmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.AuthWindow
import com.rcompany.rchat.windows.chats.ChatsWindow
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Класс-контроллер состоянием SplashWindow типа [ViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class SplashViewModel(private val userRepo: UserRepo) : ViewModel() {
    /**
     * Функция открытия окна с задержкой.
     * Принимаемые параметры - текущее окно и время задержки.
     * Устанавливает таймер на время delay и по прошествии времени проверяет, сохранены ли данные
     * пользователя в приложении (т.е. авторизован ли он). Если данные есть - открывает окно с чатами,
     * если нет - окно авторизации
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     * @param delay время задержки типа [Long]
     */
    fun openNextWindowAfterDelay(from: AppCompatActivity, delay: Long) {
        Timer().schedule(delay) {
            val to = if (!userRepo.isUserAuthorized()) AuthWindow::class.java else ChatsWindow::class.java
            from.startActivity(Intent(from, to))
            from.finish()
        }
    }

    /**
     * Функция установки текста приветствия. Проверяет, сохранены ли данные
     * пользователя в приложении (т.е. авторизован ли он). Если сохранены - устанавливает
     * именованное приветствие, если нет - стандартное приветствие
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     * @return приветственный текст типа [String]
     */
    fun getGreetingText(from: AppCompatActivity) =
        if (userRepo.isUserAuthorized())
            "${from.getString(R.string.hello_text)}, ${userRepo.getUserData()?.publicId}"
        else from.getString(R.string.greeting_default_text)
}