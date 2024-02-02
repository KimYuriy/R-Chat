package com.rcompany.rchat.windows.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rcompany.rchat.databinding.SearchUsersWindowBinding
import com.rcompany.rchat.windows.search.viewmodel.SearchViewModel
import com.rcompany.rchat.windows.search.viewmodel.SearchViewModelFactory

/**
 * Класс окна поиска пользователей
 */
class SearchUsersWindow : AppCompatActivity() {
    private lateinit var b: SearchUsersWindowBinding
    private lateinit var vm: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = SearchUsersWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = SearchViewModelFactory()
        vm = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }
}