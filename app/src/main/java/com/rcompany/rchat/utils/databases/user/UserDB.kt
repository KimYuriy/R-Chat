package com.rcompany.rchat.utils.databases.user

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
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: UserDB().also {
                    instance = it
                }
            }
    }

    /**
     * Поле данных пользователя типа [UserDataClass]
     */
    private var userData: UserDataClass? = null

    /**
     * Функция сохранения данных пользователя.
     * Записывает переданные данные в поле [userData] типа [UserDataClass]
     * @param pUserData данные пользователя типа [UserDataClass]
     */
    fun saveUserData(pUserData: UserDataClass) {
        userData = pUserData
        //TODO: Добавить сохранение в EncryptedSharedPreferences
    }

    /**
     * Функция получения данных пользователя.
     * @return поле данных пользователя [userData] типа [UserDataClass]
     */
    fun getUserData() = userData
}