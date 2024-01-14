package com.rcompany.rchat.utils.databases.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
     * Функция записи данных пользователя в поле [_userLiveData].
     * Записывает переданные данные в поле [_userLiveData] типа [UserDataClass]
     * @param userData данные пользователя типа [UserDataClass]
     */
    fun setUserData(userData: UserDataClass?) {
        _userLiveData.value = userData
    }

    /**
     * Функция сохранения данных пользователя в память приложения.
     * Преобразует данные из [_userLiveData] в JSON-объект и сохраняет объект в виде строки в
     * EncryptedSharedPreferences
     */
    fun saveUserData() {
        val obj = JSONObject().apply {
            put("id", _userLiveData.value?.id)
            put("login", _userLiveData.value?.login)
            put("avatar", _userLiveData.value?.avatar ?: JSONObject.NULL)
        }
        getEncryptedSharedPrefs().edit().putString(USER_DATA_KEY, obj.toString()).apply()
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
            UserDataClass(obj["id"] as Int, obj["login"] as String, obj["avatar"] as String?)
        } else null
    }

    /**
     * Функция получения данных пользователя.
     * @return поле данных пользователя [_userLiveData] типа [UserDataClass]
     */
    fun getUserData() = _userLiveData as LiveData<UserDataClass?>

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