package com.rcompany.rchat.windows.registration.viewmodel

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.databinding.CodeConfirmAlertBinding
import com.rcompany.rchat.utils.databases.user.UserDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.AuthWindow
import com.rcompany.rchat.utils.databases.registration.RegisterDataClass
import com.rcompany.rchat.utils.enums.ServerEndpoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        userRepo.setUserData(UserDataClass(2, "ValikSilchenko", "NotBasedPhoto"))
        Log.i("USER", userRepo.getUserData().value.toString())
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
    fun onRegisterBtnClicked(from: AppCompatActivity, data: RegisterDataClass) = CoroutineScope(Dispatchers.IO).launch {

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

    /**
     * Функция создания диалогового окна с полем ввода кода подтверждения
     * @param from окно типа [AppCompatActivity], в котором вызвана функция
     * @return диалоговое окно типа [AlertDialog]
     */
    private fun getCodeConfirmDialog(from: AppCompatActivity, endpoint: ServerEndpoints): AlertDialog {
        val b = CodeConfirmAlertBinding.inflate(from.layoutInflater)
        val dialog = AlertDialog.Builder(from).apply {
            setView(b.root)
            setNegativeButton(from.getString(R.string.cancel_text), null)
            setCancelable(false)
        }.create()
        b.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 6) {
                    //TODO: Добавить отправку кода подтверждения и получение ответа
                    dialog.cancel()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        return dialog
    }
}