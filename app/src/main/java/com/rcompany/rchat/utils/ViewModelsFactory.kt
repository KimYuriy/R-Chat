package com.rcompany.rchat.utils

import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.authorization.viewmodels.AuthViewModelFactory
import com.rcompany.rchat.windows.splash.viewmodels.SplashViewModelFactory

object ViewModelsFactory {
    fun getSplashScreenViewModel(): SplashViewModelFactory {
        val userDB = UserDB.getInstance()
        val userRepo = UserRepo.getInstance(userDB)
        return SplashViewModelFactory(userRepo)
    }

    fun getAuthViewModel(): AuthViewModelFactory {
        val userDB = UserDB.getInstance()
        val userRepo = UserRepo.getInstance(userDB)
        return AuthViewModelFactory(userRepo)
    }
}