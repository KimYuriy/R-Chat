package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных найденных пользователей, с которыми можно создать чат
 * @property publicId ID пользователя типа [String]
 * @property avatarUrl аватар пользователя в виде строки типа null-[String]
 */
data class FoundUsersDataClass(
    var publicId: String,
    var avatarUrl: String?
)