package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных, введенных при авторизации
 * @property email электронная почта пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class AuthDataClass(
    var email: String,
    var password: String
) {

    /**
     * Функция получения [Map] из данного класса
     * @return данные в формате [Map]
     */
    fun toMap() = mapOf("email" to email, "password" to password)
}