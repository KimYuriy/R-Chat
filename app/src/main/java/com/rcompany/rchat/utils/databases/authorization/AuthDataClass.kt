package com.rcompany.rchat.utils.databases.authorization

/**
 * Дата-класс данных, введенных при авторизации
 * @property email электронная почта пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class AuthDataClass(
    var email: String,
    var password: String
)