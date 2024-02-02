package com.rcompany.rchat.utils.databases.user

/**
 * Дата-класс сохраняемых данных пользователя
 * @property publicId идентификатор пользователя типа [Int]
 * @property token токен пользователя типа [String]
 */
data class UserDataClass(
    var publicId: String,
    var token: String,
)