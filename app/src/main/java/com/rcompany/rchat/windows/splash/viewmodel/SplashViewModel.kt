package com.rcompany.rchat.windows.splash.viewmodel

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.databases.user.dataclasses.UserDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.windows.authorization.AuthWindow
import com.rcompany.rchat.windows.chats.ChatsWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Класс-контроллер состоянием SplashWindow типа [ViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class SplashViewModel(private val userRepo: UserRepo) : ViewModel() {
    init {
        userRepo.loadUserMetaData()
    }
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
            CoroutineScope(Dispatchers.IO).launch {
                val to = if (!userRepo.isUserAuthorized()) AuthWindow::class.java else ChatsWindow::class.java
                if (to == ChatsWindow::class.java) {
                    val networkManager = NetworkManager(from.applicationContext, userRepo)
                    when (val state = networkManager.get(ServerEndpoints.USER_PROFILE.toString())) {
                        is ResponseState.Success -> {
                            Log.e("SplashViewModel:openNextWindowAfterDelay", "User data: ${state.data}")
                            val userData = UserDataClass.fromJson(state.data)
                            userRepo.saveUserData(userData)
                        }
                        is ResponseState.Failure -> {
                            withContext(Dispatchers.Main) {
                                Log.e("SplashViewModel:openNextWindowAfterDelay", "ERROR: ${state.errorText}")
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    from.startActivity(Intent(from, to))
                    from.finish()
                }
            }
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
        if (userRepo.isUserAuthorized()) {
            if (userRepo.getUserData() != null) {
                "${from.getString(R.string.hello_text)}, ${userRepo.getUserData()?.first_name}!"
            } else {
                from.getString(R.string.successfully_authorized_text)
            }
        }
        else from.getString(R.string.greeting_default_text)
}