package com.rcompany.rchat.windows.search.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.enums.ServerEndpoints
import com.rcompany.rchat.utils.network.Requests
import com.rcompany.rchat.utils.network.ResponseState
import com.rcompany.rchat.utils.network.address.dataclass.UserMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val userRepo: UserRepo): ViewModel() {
    fun searchUser(login: String, from: AppCompatActivity) = CoroutineScope(Dispatchers.IO).launch {
        var deviceFingerprint = ""
        FingerprinterFactory.create(from).getFingerprint(version = Fingerprinter.Version.V_5) {
            deviceFingerprint = it
        }
        when (val state = Requests.get(
            login,
            UserMetadata(deviceFingerprint, userRepo.getUserData()?.accessToken),
            ServerEndpoints.SEARCH_USER.toString()
        )) {
            is ResponseState.Success -> {
                Log.d("Search", state.data.toString())
            }
            is ResponseState.Failure -> {
                Log.e("Search", state.errorText)
            }
        }
    }
}