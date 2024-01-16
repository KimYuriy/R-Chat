package com.rcompany.rchat.windows.authorization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабричный класс для ViewModel [AuthViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(userRepo) as T
}