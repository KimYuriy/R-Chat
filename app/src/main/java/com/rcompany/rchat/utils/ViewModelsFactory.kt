package com.rcompany.rchat.utils

import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.viewmodels.AuthViewModelFactory
import com.rcompany.rchat.windows.registration.viewmodels.RegisterViewModelFactory
import com.rcompany.rchat.windows.splash.viewmodels.SplashViewModelFactory

/**
 * Объект, содержащий функции создания фабрик классов для других различных классов
 */
object ViewModelsFactory {

    /**
     * Функция получения фабрики ViewModel для SplashWindow.
     * Создает экземпляры БД пользователя типа [UserDB] и репозитория БД пользователя типа [UserRepo]
     * @return фабрика SplashViewModel типа [SplashViewModelFactory]
     */
    fun getSplashScreenViewModel(): SplashViewModelFactory {
        val userDB = UserDB.getInstance()
        val userRepo = UserRepo.getInstance(userDB)
        return SplashViewModelFactory(userRepo)
    }

    /**
     * Функция получения фабрики ViewModel для AuthWindow.
     * Создает экземпляры БД пользователя типа [UserDB] и репозитория БД пользователя типа [UserRepo]
     * @return фабрика AuthWindow типа [AuthViewModelFactory]
     */
    fun getAuthViewModel(): AuthViewModelFactory {
        val userDB = UserDB.getInstance()
        val userRepo = UserRepo.getInstance(userDB)
        return AuthViewModelFactory(userRepo)
    }

    /**
     * Функция получения фабрики ViewModel для RegisterWindow.
     * Создает экземпляры БД пользователя типа [UserDB] и репозитория БД пользователя типа [UserRepo]
     * @return фабрика RegisterWindow типа [RegisterViewModelFactory]
     */
    fun getRegisterViewModel(): RegisterViewModelFactory {
        val userDB = UserDB.getInstance()
        val userRepo = UserRepo.getInstance(userDB)
        return RegisterViewModelFactory(userRepo)
    }
}