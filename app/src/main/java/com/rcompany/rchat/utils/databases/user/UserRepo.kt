package com.rcompany.rchat.utils.databases.user

import com.rcompany.rchat.utils.databases.user.dataclasses.UserDataClass
import com.rcompany.rchat.utils.databases.user.dataclasses.UserMetaDataClass

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
     * Функция получения данных пользователя.
     * Вызывает метод [getUserMetaData] из БД пользователя
     * @return данные пользователя из БД пользователя
     */
    fun getUserMetaData() = userDB.getUserMetaData()

    /**
     * Функция сохранения данных пользователя
     * @param userData данные пользователя типа [UserMetaDataClass]
     */
    fun saveUserMetaData(userData: UserMetaDataClass) = userDB.saveUserMetaData(userData)

    /**
     * Функция загрузки данных пользователя
     */
    fun loadUserMetaData() {
        userDB.loadUserMetaData()
    }

    fun saveUserData(data: UserDataClass) = userDB.saveUserData(data)

    fun getUserData() = userDB.getUserData()


    /**
     * Функция проверки авторизации пользователя.
     * Вызывается метод [getUserMetaData] и проверяется полученное значение - если оно не равно null,
     * то пользователь считается авторизованным, иначе - не авторизован
     * @return true/false типа [Boolean]
     */
    fun isUserAuthorized() = getUserMetaData() != null
}