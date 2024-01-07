package com.rcompany.rchat.utils.databases.user

/**
 * Репозиторий БД пользователя.
 * @property userDB база данных пользователя типа [UserDB]
 */
class UserRepo private constructor(private val userDB: UserDB) {
    companion object {

        /**
         * Экземпляр репозитория типа null-[UserRepo]
         */
        @Volatile
        private var instance: UserRepo? = null

        /**
         * Функция получения экземпляра репозитория. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @param userDB база данных пользователя типа [UserDB]
         * @return экземпляр репозитория [instance] типа [UserRepo]
         */
        fun getInstance(userDB: UserDB) =
            instance ?: synchronized(this) {
                instance ?: UserRepo(userDB).also {
                    instance = it
                }
            }
    }

    /**
     * Функция сохранения данных пользователя.
     * Вызывает метод [setUserData] из БД пользователя
     * @param userData данные пользователя типа [UserDataClass]
     */
    fun setUserData(userData: UserDataClass) = userDB.setUserData(userData)

    /**
     * Функция получения данных пользователя.
     * Вызывает метод [getUserData] из БД пользователя
     * @return данные пользователя из БД пользователя
     */
    fun getUserData() = userDB.getUserData()

    /**
     * Функция проверки авторизации пользователя.
     * Вызывается метод [getUserData] и проверяется полученное значение - если оно не равно null,
     * то пользователь считается авторизованным, иначе - не авторизован
     * @return true/false типа [Boolean]
     */
    fun isUserAuthorized() = getUserData() != null
}