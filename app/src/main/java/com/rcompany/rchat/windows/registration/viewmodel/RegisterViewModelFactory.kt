package com.rcompany.rchat.windows.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

/**
 * Фабричный класс для ViewModel [RegisterViewModel]
 * @property userRepo репозиторий БД пользователя типа [UserRepo]
 */
@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegisterViewModel(userRepo) as T
}