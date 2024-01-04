package com.rcompany.rchat.utils.databases.user

class UserDB private constructor() {
    companion object {
        @Volatile
        private var instance: UserDB? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: UserDB().also {
                    instance = it
                }
            }
    }

    private var userData: UserDataClass? = null

    fun saveUserData(pUserData: UserDataClass) {
        userData = pUserData
        //TODO: Добавить сохранение в EncryptedSharedPreferences
    }

    fun getUserData() = userData
}