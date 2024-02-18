package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных найденных пользователей, с которыми можно создать чат
 * @property userId ID пользователя типа [String]
 * @property login логин пользователя типа [String]
 * @property email почта пользователя типа [String]
 * @property avatarString аватар пользователя в виде строки типа null-[String]
 */
data class FoundUsersDataClass(
    var userId: String,
    var login: String?,
    var email: String?,
    var avatarString: String?
)