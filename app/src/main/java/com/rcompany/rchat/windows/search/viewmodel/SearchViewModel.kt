package com.rcompany.rchat.windows.search.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.utils.JasonStatham
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Класс-контроллер окна SearchUserWindow
 * @property userRepo репозиторий данных пользователя типа [UserRepo]
 */
class SearchViewModel(private val userRepo: UserRepo): ViewModel() {
    private val _foundUsersList = ArrayList<FoundUsersDataClass>()
    val foundUsersLiveData = MutableLiveData<ArrayList<FoundUsersDataClass>>()

    /**
     * Получение списка пользователей по запросу
     * @param login имя пользователя типа [String]
     * @param from контекст вызова запроса типа [AppCompatActivity]
     */
    fun searchUser(login: String, from: AppCompatActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(from.applicationContext, userRepo).get(
                ServerEndpoints.SEARCH_USER.toString(),
                mapOf("match_str" to login)
            )) {
                is ResponseState.Success -> {
                    withContext(Dispatchers.Main) {
                        updateFoundUsers(state.data)
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("SearchViewModel:searchUser", "${state.code}: ${state.errorText}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(from,
                            from.getString(R.string.users_not_found_text), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Функция обновления списка пользователей
     * @param source список найденных пользователей типа [JSONObject]
     */
    private fun updateFoundUsers(source: JSONObject) {
        if (_foundUsersList.isNotEmpty()) _foundUsersList.clear()
        Log.d("SearchViewModel:updateFoundUsers", "$source")
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