package com.rcompany.rchat.utils.databases.user

class UserRepo private constructor(private val userDB: UserDB) {
    companion object {
        @Volatile
        private var instance: UserRepo? = null

        /**
         * Функция получения экземпляра БД.
         *
         */
        fun getInstance(userDB: UserDB) =
            instance ?: synchronized(this) {
                instance ?: UserRepo(userDB).also {
                    instance = it
                }
            }
    }

    fun saveUserData(id: Int, login: String, avatar: String?) {
        userDB.saveUserData(UserDataClass(id, login, avatar))
    }

    fun getUserData() = userDB.getUserData()

    fun isUserAuthorized() = getUserData() != null
}