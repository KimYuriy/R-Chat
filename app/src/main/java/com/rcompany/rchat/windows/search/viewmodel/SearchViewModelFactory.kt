package com.rcompany.rchat.windows.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.utils.databases.user.UserRepo

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) = SearchViewModel(userRepo) as T
}