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
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.databases.window_dataclasses.AuthDataClass
import com.rcompany.rchat.windows.authorization.AuthWindow
import com.rcompany.rchat.utils.databases.window_dataclasses.RegisterDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.utils.network.token.Tokens
import com.rcompany.rchat.windows.chats.ChatsWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    fun onRegisterClicked(from: AppCompatActivity, data: RegisterDataClass) {
        CoroutineScope(Dispatchers.IO).launch {
            val networkRepo = NetworkManager(from.applicationContext, userRepo)
            when (val state = networkRepo.post(
                ServerEndpoints.REGISTER.toString(),
                data.toMap(),
            )) {
                is ResponseState.Success -> {
                    Log.d("RegisterViewModel:onRegisterClicked", "Registration successful")
                    val responseData = state.data
                    val stringedResponseData = responseData.toString()
                    if (stringedResponseData.length == 2) {
                        val authData = AuthDataClass(
                            data.email,
                            data.password
                        ).toMap()
                        when (val authState = networkRepo.post(
                            ServerEndpoints.AUTH.toString(),
                            authData,
                        )) {
                            is ResponseState.Success -> {
                                Log.d("RegisterViewModel:onRegisterClicked", "Authorization successful")
                                val userData = Tokens(authState.data).parseToken()
                                withContext(Dispatchers.Main) {
                                    userRepo.saveUserData(userData)
                                    from.startActivity(Intent(from, ChatsWindow::class.java))
                                    from.finish()
                                }
                            }
                            is ResponseState.Failure -> {
                                Log.e("RegisterViewModel:onRegisterClicked", "Authorization failed")
                                withContext(Dispatchers.Main) {
                                    val dialog = getFailureDialog(from, authState)
                                    dialog.show()
                                }
                            }
                        }
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("RegisterViewModel:onRegisterClicked", "Registration failed")
                    withContext(Dispatchers.Main) {
                        val dialog = getFailureDialog(from, state)
                        dialog.show()
                    }
                }
            }
        }
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
        if (source.length < 7) return context.getString(R.string.short_password_text)
        return null
    }

    fun getPublicIdHelperText(context: Context, source: String): String? {
        if (source.isEmpty()) return context.getString(R.string.required_text)
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

    /**
     * Функция создания диалогового окна с кодом и текстом ошибки
     */
    private fun getFailureDialog(from: AppCompatActivity, state: ResponseState.Failure) = AlertDialog.Builder(from).apply {
            setCancelable(true)
            setTitle("${from.getString(R.string.error_text)} ${state.code}")
            setMessage(state.errorText)
            setPositiveButton(from.getString(R.string.ok_text), null)
        }.create()
}