package com.rcompany.rchat.windows.authorization.viewmodel

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
import com.rcompany.rchat.databinding.PasswordRecoveryAlertBinding
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.databases.window_dataclasses.AuthDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.utils.network.token.Tokens
import com.rcompany.rchat.windows.chats.ChatsWindow
import com.rcompany.rchat.windows.registration.RegisterWindow
import com.rcompany.rchat.windows.splash.SplashWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        if (source.length < 7) return context.getString(R.string.short_password_text)
        return null
    }

    /**
     * Функция авторизации в приложении.
     * @param from окно типа [AppCompatActivity], в котором была вызвана функция
     * @param data данные авторизации типа [AuthDataClass]
     */
    fun onLoginClicked(from: AppCompatActivity, data: AuthDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(from.applicationContext, userRepo).post(
                ServerEndpoints.AUTH.toString(),
                data.toMap(),
                false
            )) {
                is ResponseState.Success -> {
                    Log.d("AuthViewModel:onLoginClicked", "Auth successful")
                    val userData = Tokens(state.data).parseToken()
                    withContext(Dispatchers.Main) {
                        userRepo.saveUserMetaData(userData)
                        from.startActivity(Intent(from, SplashWindow::class.java))
                        from.finish()
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("AuthViewModel:onLoginClicked", "Auth failed")
                    withContext(Dispatchers.Main) {
                        val dialog = getFailureDialog(from, state)
                        dialog.show()
                    }
                }
            }
        }
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
        val dialog = getRecoveryPasswordDialog(from)
        dialog.show()
    }

    /**
     * Функция создания диалогового окна сброса пароля
     * @param from окно типа [AppCompatActivity], в котором вызвана функция
     * @return диалоговое окно типа [AlertDialog.Builder]
     */
    private fun getRecoveryPasswordDialog(from: AppCompatActivity): AlertDialog {
        val b = PasswordRecoveryAlertBinding.inflate(from.layoutInflater)
        val dialog = AlertDialog.Builder(from).apply {
            setView(b.root)
            setPositiveButton(from.getString(R.string.send_text), null) //TODO: Добавить событие на нажатие кнопки
            setNegativeButton(from.getString(R.string.cancel_text), null)
            setCancelable(true)
        }.create()
        return dialog
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

    /**
     * Функция показа диалогового окна с кодом и текстом ошибки
     * @param from окно типа [AppCompatActivity]
     * @param state состояние диалога типа [ResponseState.Failure]
     * @return диалоговое окно типа [AlertDialog]
     */
    private fun getFailureDialog(from: AppCompatActivity, state: ResponseState.Failure) = AlertDialog.Builder(from).apply {
        setCancelable(true)
        setTitle("${from.getString(R.string.error_text)} ${state.code}")
        setMessage(state.errorText)
        setPositiveButton(from.getString(R.string.ok_text), null)
    }.create()
}