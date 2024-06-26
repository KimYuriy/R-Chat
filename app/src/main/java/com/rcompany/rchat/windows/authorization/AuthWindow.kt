package com.rcompany.rchat.windows.authorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.AuthWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.viewmodel.AuthViewModel
import com.rcompany.rchat.windows.authorization.viewmodel.AuthViewModelFactory
import com.rcompany.rchat.utils.databases.window_dataclasses.AuthDataClass
import com.rcompany.rchat.utils.network.NetworkManager

/**
 * Класс окна авторизации типа [AppCompatActivity]
 */
class AuthWindow : AppCompatActivity() {
    private lateinit var b: AuthWindowBinding
    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = AuthWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = AuthViewModelFactory(
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        /**
         * Отслеживание изменений в поле ввода e-mail
         */
        b.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilEmail.helperText = vm.getEmailHelperText(this@AuthWindow, s.toString())
                setAuthBtnEnabled()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        
        /**
         * Отслеживание изменений в поле ввода пароля
         */
        b.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilPassword.helperText = vm.getPasswordHelperText(this@AuthWindow, s.toString())
                setAuthBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        /**
         * Обработка нажатия кнопки авторизации
         */
        b.btnAuthorize.setOnClickListener {
            vm.onLoginClicked(this, AuthDataClass(b.etEmail.text.toString(), b.etPassword.text.toString()))
        }

        /**
         * Обработка нажатия кнопки регистрации
         */
        b.btnRegistration.setOnClickListener {
            vm.onRegisterClicked(this)
        }

        /**
         * Обработка нажатия кнопки восстановления пароля
         */
        b.tvForgotPassword.setOnClickListener {
            vm.onForgotPasswordClicked(this)
        }
    }

    /**
     * Функция установки доступности кнопки авторизации.
     * Если тексты-подсказки у полей e-mail и пароля не null, то кнопка становится активной, в
     * противном случае она становится неактивной
     * @return true/false [Boolean]
     */
    private fun setAuthBtnEnabled() {
        b.btnAuthorize.isEnabled = b.tilEmail.helperText == null && b.tilPassword.helperText == null
    }
}