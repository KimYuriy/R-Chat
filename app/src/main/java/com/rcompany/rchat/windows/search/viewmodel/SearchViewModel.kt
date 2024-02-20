package com.rcompany.rchat.windows.search.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.rcompany.rchat.utils.JasonStatham
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass
import com.rcompany.rchat.utils.enums.ServerEndpoints
import com.rcompany.rchat.utils.network.Requests
import com.rcompany.rchat.utils.network.ResponseState
import com.rcompany.rchat.utils.network.address.dataclass.UserMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SearchViewModel(private val userRepo: UserRepo): ViewModel() {
    private val _foundUsersList = ArrayList<FoundUsersDataClass>()
    val foundUsersLiveData = MutableLiveData<ArrayList<FoundUsersDataClass>>()

    fun searchUser(login: String, from: AppCompatActivity) = CoroutineScope(Dispatchers.IO).launch {
        var deviceFingerprint = ""
        FingerprinterFactory.create(from).getFingerprint(version = Fingerprinter.Version.V_5) {
            deviceFingerprint = it
        }
        when (val state = Requests(from.applicationContext).get(
            login,
            UserMetadata(deviceFingerprint, userRepo.getUserData()?.accessToken),
            ServerEndpoints.SEARCH_USER.toString()
        )) {
            is ResponseState.Success -> {
                withContext(Dispatchers.Main) {
                    updateFoundUsers(state.data)
                }
            }
            is ResponseState.Failure -> {
                Log.e("Search", state.errorText)
            }
        }
    }

    private fun updateFoundUsers(source: JSONObject) {
        if (_foundUsersList.isNotEmpty()) _foundUsersList.clear()
        val usersList = JasonStatham.string2ListJSONs(source["users"].toString())
        for (user in usersList) {
            val avatar = if (user["avatar_url"].toString() == "null") null
                else user["avatar_url"].toString()
            _foundUsersList.add(FoundUsersDataClass(
                user["public_id"].toString(),
                avatar
            ))
        }
        foundUsersLiveData.value = _foundUsersList
    }
}