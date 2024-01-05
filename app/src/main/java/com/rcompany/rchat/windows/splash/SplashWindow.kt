package com.rcompany.rchat.windows.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.SplashWindowBinding
import com.rcompany.rchat.utils.ViewModelsFactory
import com.rcompany.rchat.windows.splash.viewmodels.SplashViewModel

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

        vm = ViewModelProvider(this, ViewModelsFactory.getSplashScreenViewModel())[SplashViewModel::class.java]

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