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

class AuthViewModel(private val userRepo: UserRepo): ViewModel() {

    /**
     * Функция определения текста-подсказки для поля e-mail.
     * Если введенный текст не соответствует паттерну e-mail (нет символа '@' или '.'), то возвращает
     * текст ошибки, в противном случае возвращает null
     */
    fun getEmailHelperText(context: Context, source: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(source).matches()) return context.getString(R.string.incorrect_email_text)
        return null
    }

    /**
     * Функция определения текста-подсказки для поля пароля.
     * Если длина введенного текста менее 4, то возвращает текст ошибки, в противном случае
     * возвращает null
     */
    fun getPasswordHelperText(context: Context, source: String): String? {
        if (source.length < 4) return context.getString(R.string.short_password_text)
        return null
    }

    /**
     * Функция авторизации в приложении.
     */
    fun onLoginClicked(from: AppCompatActivity, data: AuthDataClass) = GlobalScope.launch(Dispatchers.IO) {

    }

    /**
     * Функция открытия окна регистрации
     */
    fun onRegisterClicked(from: AppCompatActivity) {
        from.apply {
            startActivity(Intent(from, RegisterWindow::class.java))
            finish()
        }
    }

    /**
     * Функция открытия окна восстановления пароля
     */
    fun onForgotPasswordClicked(from: AppCompatActivity) {

    }
}