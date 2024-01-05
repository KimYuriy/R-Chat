package com.rcompany.rchat.windows.registration.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.AuthWindow
import com.rcompany.rchat.windows.registration.viewmodels.data.RegisterDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Класс-контроллер состоянием RegisterWindow типа [ViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
class RegisterViewModel(private val userRepo: UserRepo): ViewModel() {

    /**
     * Функция открытия окна авторизации.
     * @param from окно типа [AppCompatActivity], в котором вызвана функция
     */
    fun onAuthClicked(from: AppCompatActivity) {
        from.apply {
            startActivity(Intent(from, AuthWindow::class.java))
            finish()
        }
    }

    /**
     * Функция регистрации.
     * @param from окно типа [AppCompatActivity], в котором вызвана функция
     * @param data данные, введенные пользователем при регистрации типа [RegisterDataClass]
     */
    fun onRegisterBtnClicked(from: AppCompatActivity, data: RegisterDataClass) = GlobalScope.launch(Dispatchers.IO) {

    }

    /**
     * Функция определения текста-подсказки для поля e-mail.
     * Если введенный текст не соответствует паттерну e-mail (нет символа '@' или '.'), то возвращает
     * текст ошибки, в противном случае возвращает null
     * @param context контекст, в котором происходит работа функции типа [Context]
     * @param source проверяемая на соответствие паттерну строка типа [String]
     * @return строка с текстом ошибки типа [String] или null
     */
    fun getEmailHelperText(context: Context, source: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(source).matches()) return context.getString(R.string.incorrect_email_text)
        return null
    }

    /**
     * Функция определения текста-подсказки для поля пароля.
     * Если длина введенного текста менее 4, то возвращает текст ошибки, в противном случае
     * возвращает null
     * @param context контекст, в котором происходит работа функции типа [Context]
     * @param source проверяемая на длину строка типа [String]
     * @return строка с текстом ошибки типа [String] или null
     */
    fun getPasswordHelperText(context: Context, source: String): String? {
        if (source.length < 4) return context.getString(R.string.short_password_text)
        return null
    }
}