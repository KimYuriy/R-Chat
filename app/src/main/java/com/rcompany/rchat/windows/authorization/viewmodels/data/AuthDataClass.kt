package com.rcompany.rchat.windows.authorization.viewmodels.data

/**
 * Дата-класс данных, введенных при авторизации
 * @property email электронная почта пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class AuthDataClass(
    var email: String,
    var password: String
)