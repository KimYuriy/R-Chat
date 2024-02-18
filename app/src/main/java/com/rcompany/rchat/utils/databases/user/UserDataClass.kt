package com.rcompany.rchat.utils.databases.user

/**
 * Дата-класс сохраняемых данных пользователя
 * @property publicId идентификатор пользователя типа [Int]
 * @property accessToken токен пользователя типа [String]
 */
data class UserDataClass(
    var publicId: String,
    var accessToken: String,
    var refreshToken: String
)