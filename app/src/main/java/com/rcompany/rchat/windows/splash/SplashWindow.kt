package com.rcompany.rchat.windows.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.SplashWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.splash.viewmodels.SplashViewModel
import com.rcompany.rchat.windows.splash.viewmodels.SplashViewModelFactory

/**
 * Класс окна приветственного экрана типа [AppCompatActivity]
 */
class SplashWindow : AppCompatActivity() {
    private lateinit var b: SplashWindowBinding
    private lateinit var vm: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = SplashWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = SplashViewModelFactory(
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[SplashViewModel::class.java]

        /**
         * Установка приветственного текста
         */
        b.tvGreeting.text = vm.getGreetingText(this)

        /**
         * Открытие следующего окна после задержки
         */
        vm.openNextWindowAfterDelay(this, 3000L)
    }
}