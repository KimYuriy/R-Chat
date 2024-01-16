package com.rcompany.rchat.windows.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабричный класс для ViewModel [SplashViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SplashViewModel(userRepo) as T
}