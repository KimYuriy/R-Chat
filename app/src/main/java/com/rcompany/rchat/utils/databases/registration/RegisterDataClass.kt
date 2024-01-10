package com.rcompany.rchat.utils.databases.registration

/**
 * Дата-класс данных, введенных при регистрации
 * @property email почта пользователя типа [String]
 * @property login логин пользователя типа [String]
 * @property uniqueID уникальный ID пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class RegisterDataClass(
    var email: String,
    var login: String,
    var uniqueID: String,
    var password: String
)