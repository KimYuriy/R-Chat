package com.rcompany.rchat.windows.authorization.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.viewmodels.data.AuthDataClass
import com.rcompany.rchat.windows.registration.RegisterWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Класс-контроллер состоянием AuthWindow типа [ViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class AuthViewModel(private val userRepo: UserRepo): ViewModel() {

    /**
     * Функция определения текста-подсказки для поля e-mail.
     * Если введенный текст не соответствует паттерну e-mail (нет символа '@' или '.'), то возвращает
     * текст ошибки, в противном случае возвращает null
     * @param context контекст приложения типа [Context], в котором выполняется функция
     * @param source отслеживаемая строка типа [String]
     * @return строка с текстом ошибки или null
     */
    fun getEmailHelperText(context: Context, source: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(source).matches()) return context.getString(R.string.incorrect_email_text)
        return null
    }

    /**
     * Функция определения текста-подсказки для поля пароля.
     * Если длина введенного текста менее 4, то возвращает текст ошибки, в противном случае
     * возвращает null
     * @param context контекст приложения типа [Context], в котором выполняется функция
     * @param source отслеживаемая строка типа [String]
     * @return строка с текстом ошибки или null
     */
    fun getPasswordHelperText(context: Context, source: String): String? {
        if (source.length < 4) return context.getString(R.string.short_password_text)
        return null
    }

    /**
     * Функция авторизации в приложении.
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     * @param data данные авторизации типа [AuthDataClass]
     */
    fun onLoginClicked(from: AppCompatActivity, data: AuthDataClass) = GlobalScope.launch(Dispatchers.IO) {

    }

    /**
     * Функция открытия окна регистрации
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     */
    fun onRegisterClicked(from: AppCompatActivity) {
        from.apply {
            startActivity(Intent(from, RegisterWindow::class.java))
            finish()
        }
    }

    /**
     * Функция открытия окна восстановления пароля
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     */
    fun onForgotPasswordClicked(from: AppCompatActivity) {

    }
}