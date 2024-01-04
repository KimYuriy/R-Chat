package com.rcompany.rchat.windows.splash.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SplashViewModel(userRepo) as T
}