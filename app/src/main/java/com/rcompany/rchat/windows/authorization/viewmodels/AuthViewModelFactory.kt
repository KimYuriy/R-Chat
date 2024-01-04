package com.rcompany.rchat.windows.authorization.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(userRepo) as T
}