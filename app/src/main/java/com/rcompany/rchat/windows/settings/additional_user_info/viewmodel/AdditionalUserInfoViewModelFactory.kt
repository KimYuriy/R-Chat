package com.rcompany.rchat.windows.settings.additional_user_info.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

@Suppress("UNCHECKED_CAST")
class AdditionalUserInfoViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = AdditionalUserInfoViewModel(userRepo) as T
}