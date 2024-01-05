package com.rcompany.rchat.utils.databases.user

/**
 * Дата-класс сохраняемых данных пользователя
 * @property id идентификатор пользователя типа [Int]
 * @property login логин пользователя типа [String]
 * @property avatar строка аватарки пользователя типа null-[String]
 */
data class UserDataClass(
    var id: Int,
    var login: String,
    var avatar: String?
)