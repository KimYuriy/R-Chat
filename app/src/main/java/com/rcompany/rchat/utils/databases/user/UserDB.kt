package com.rcompany.rchat.utils.databases.user

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONObject

/**
 * База данных пользователя
 */
class UserDB private constructor() {
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
        fun getInstance(): UserDB {
            return instance ?: synchronized(this) {
                instance ?: UserDB().also {
                    instance = it
                }
            }
        }

        private const val USER_PREFS_KEY = "USER_PREFS"
        private const val USER_DATA_KEY = "USER_DATA"
    }

    /**
     * Функция записи данных пользователя в поле [userData].
     * Записывает переданные данные в поле [userData] типа [UserDataClass]
     * @param pUserData данные пользователя типа [UserDataClass]
     */
    fun setUserData(pUserData: UserDataClass?) {
        userData = pUserData
    }

    /**
     * Функция сохранения данных пользователя в память приложения.
     * Преобразует данные из [userData] в JSON-объект и сохраняет объект в виде строки в
     * EncryptedSharedPreferences
     * @param context контекст типа [Context], в котором происходит сохранение
     */
    fun saveUserData(context: Context) {
        val obj = JSONObject().apply {
            put("id", userData?.id)
            put("login", userData?.login)
            put("avatar", userData?.avatar ?: JSONObject.NULL)
        }
        getEncryptedSharedPrefs(context).edit().putString(USER_DATA_KEY, obj.toString()).apply()
    }

    /**
     * Функция загрузки сохраненных в EncryptedSharedPreferences данных пользователя.
     * Загружается строка и, если она не null, преобразуется в JSON-объект и сохраняется в
     * [userData] с соответствующими полями
     * @param context контекст типа [Context], в котором происходит сохранение
     */
    fun loadUserData(context: Context) {
        val data = getEncryptedSharedPrefs(context).getString(USER_DATA_KEY, null)
        userData = if (data != null) {
            val obj = JSONObject(data)
            UserDataClass(obj["id"] as Int, obj["login"] as String, obj["avatar"] as String?)
        }
        else null
    }

    /**
     * Функция получения данных пользователя.
     * @return поле данных пользователя [userData] типа [UserDataClass]
     */
    fun getUserData() = userData

    /**
     * Функция получения зашифрованной SharedPreferences
     * @param context контекст типа [Context], в котором происходит вызов SharedPreferences
     * @return БД типа [SharedPreferences]
     */
    private fun getEncryptedSharedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context).apply {
            setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        }.build()
        return EncryptedSharedPreferences.create(
            context,
            USER_PREFS_KEY,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Поле данных пользователя типа [UserDataClass]
     */
    private var userData: UserDataClass? = null
}