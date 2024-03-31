package com.rcompany.rchat.utils.databases.user

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.rcompany.rchat.utils.databases.user.dataclasses.UserDataClass
import org.json.JSONObject

/**
 * База данных пользователя
 */
class UserDB private constructor(private val applicationContext: Context) {
    companion object {
        /**
         * Экземпляр БД типа null-[UserDB]
         */
        @Volatile
        private var instance: UserDB? = null

        /**
         * Функция получения экземпляра БД. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @return экземпляр БД [instance] типа [UserDB]
         */
        fun getInstance(applicationContext: Context) =
            instance ?: synchronized(this) {
                instance ?: UserDB(applicationContext).also {
                    instance = it
                }
            }

        private const val USER_PREFS_KEY = "USER_PREFS"
        private const val USER_DATA_KEY = "USER_DATA"
    }

    /**
     * Поле данных пользователя типа [UserDataClass]
     */
    private val _userLiveData = MutableLiveData<UserDataClass>()

    /**
     * Функция сохранения данных пользователя в память приложения.
     * Преобразует данные из [_userLiveData] в JSON-объект и сохраняет объект в виде строки в
     * EncryptedSharedPreferences
     */
    fun saveUserData(userData: UserDataClass) {
        _userLiveData.value = userData
        Log.d("UserDB:saveUserData", userData.toString())
        getEncryptedSharedPrefs().edit().putString(USER_DATA_KEY, userData.toJson().toString()).apply()
    }

    /**
     * Функция загрузки сохраненных в EncryptedSharedPreferences данных пользователя.
     * Загружается строка и, если она не null, преобразуется в JSON-объект и сохраняется в
     * [_userLiveData] с соответствующими полями
     */
    fun loadUserData() {
        val data = getEncryptedSharedPrefs().getString(USER_DATA_KEY, null)
        _userLiveData.value = if (data != null) {
            val obj = JSONObject(data)
            Log.d("UserDB:loadUserData", obj.toString())
            UserDataClass.fromJson(obj)
        } else null
    }

    /**
     * Функция получения данных пользователя.
     * @return поле данных пользователя [_userLiveData] типа [UserDataClass]
     */
    fun getUserData() = _userLiveData.value

    /**
     * Функция получения зашифрованной SharedPreferences
     * @return БД типа [SharedPreferences]
     */
    private fun getEncryptedSharedPrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(applicationContext).apply {
            setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        }.build()
        return EncryptedSharedPreferences.create(
            applicationContext,
            USER_PREFS_KEY,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}