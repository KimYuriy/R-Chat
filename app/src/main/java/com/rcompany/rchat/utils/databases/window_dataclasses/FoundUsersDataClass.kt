package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных найденных пользователей, с которыми можно создать чат
 * @property id ID пользователя типа [Int]
 * @property login логин пользователя типа [String]
 * @property email почта пользователя типа [String]
 * @property avatarString аватар пользователя в виде строки типа null-[String]
 */
data class FoundUsersDataClass(
    var id: Int,
    var login: String,
    var email: String?,
    var avatarString: String?
)