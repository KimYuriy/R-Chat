package com.rcompany.rchat.windows.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.utils.enums.ServerEndpoints
import com.rcompany.rchat.utils.network.Requests
import com.rcompany.rchat.utils.network.ResponseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    fun searchUser(login: String) = CoroutineScope(Dispatchers.IO).launch {
        when (val state = Requests.get(login, ServerEndpoints.SEARCH_USER.toString())) {
            is ResponseState.Success -> {
                Log.d("Search", state.data.toString())
            }
            is ResponseState.Failure -> {
                Log.e("Search", state.errorText)
            }
        }
    }
}