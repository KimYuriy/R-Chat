package com.rcompany.rchat.windows.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.RegistrationWindowBinding
import com.rcompany.rchat.utils.ViewModelsFactory
import com.rcompany.rchat.windows.registration.viewmodels.RegisterViewModel
import com.rcompany.rchat.windows.registration.viewmodels.data.RegisterDataClass

class RegisterWindow : AppCompatActivity() {
    private lateinit var b: RegistrationWindowBinding
    private lateinit var vm: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = RegistrationWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        vm = ViewModelProvider(this, ViewModelsFactory.getRegisterViewModel())[RegisterViewModel::class.java]

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

        b.etLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO: Добавить отслеживание изменений в поле ввода логина
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        b.etUniqueID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO: Добавить отслеживание изменений в поле ввода уникального ID
                setRegisterBtnEnabled()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

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

        b.tvAlreadyHaveAccount.setOnClickListener {
            vm.onAuthClicked(this)
        }

        b.btnRegister.setOnClickListener {
            vm.onRegisterBtnClicked(
                this,
                RegisterDataClass(
                    b.etEmail.text.toString(),
                    b.etLogin.text.toString(),
                    b.etUniqueID.text.toString(),
                    b.etPassword.text.toString()
                )
            )
        }
    }

    private fun setRegisterBtnEnabled() {
        b.btnRegister.isEnabled =
            b.tilEmail.helperText == null && b.tilLogin.helperText == null &&
                    b.tilUniqueID.helperText == null && b.tilPassword.helperText == null &&
                    b.tilRepeatPassword.helperText == null
    }
}