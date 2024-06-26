package com.rcompany.rchat.windows.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.RegistrationWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.registration.viewmodel.RegisterViewModel
import com.rcompany.rchat.windows.registration.viewmodel.RegisterViewModelFactory
import com.rcompany.rchat.utils.databases.window_dataclasses.RegisterDataClass

/**
 * Класс окна регистрации типа [AppCompatActivity]
 */
class RegisterWindow : AppCompatActivity() {
    private lateinit var b: RegistrationWindowBinding
    private lateinit var vm: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = RegistrationWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = RegisterViewModelFactory(
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        /**
         * Отслеживание изменений в поле ввода e-mail
         */
        b.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilEmail.helperText = vm.getEmailHelperText(this@RegisterWindow, s.toString())
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        /**
         * Отслеживание изменений в поле ввода публичного ID
         */
        b.etPublicID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilPublicID.helperText = vm.getPublicIdHelperText(this@RegisterWindow, s.toString())
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        /**
         * Отслеживание изменений в поле ввода пароля
         */
        b.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilPassword.helperText = vm.getPasswordHelperText(this@RegisterWindow, s.toString())
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        /**
         * Отслеживание изменений в поле ввода повторения пароля
         */
        b.etRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                b.tilRepeatPassword.helperText = vm.getPasswordHelperText(this@RegisterWindow, s.toString())
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        /**
         * Обработка нажатия текста авторизации
         */
        b.tvAlreadyHaveAccount.setOnClickListener {
            vm.onAuthClicked(this)
        }

        /**
         * Обработка нажатия текста авторизации
         */
        b.btnRegister.setOnClickListener {
            vm.onRegisterClicked(
                this,
                RegisterDataClass(
                    b.etFirstName.text.toString(),
                    b.etEmail.text.toString(),
                    b.etPublicID.text.toString(),
                    b.etPassword.text.toString()
                )
            )
        }
    }

    /**
     * Функция установки активности кнопки.
     * Если все тексты-подсказки равны null (т.е. все введенные в поля тексты корректны и проходят
     * по условиям), то кнопка становится активной, в противном случае - неактивной
     * @return true/false [Boolean]
     */
    private fun setRegisterBtnEnabled() {
        b.btnRegister.isEnabled =
            b.tilEmail.helperText == null && b.tilPublicID.helperText == null &&
                    b.tilPassword.helperText == null && b.tilRepeatPassword.helperText == null &&
                    b.etPassword.text.toString() == b.etRepeatPassword.text.toString()
    }
}