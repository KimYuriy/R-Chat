package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных, введенных при регистрации
 * @property email почта пользователя типа [String]
 * @property publicId уникальный ID пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class RegisterDataClass(
    val firstName: String,
    var email: String,
    var publicId: String,
    var password: String
) {

    /**
     * Функция получения [Map] из данного класса
     * @return данные в формате [Map]
     */
    fun toMap() = mapOf(
        "first_name" to firstName,
        "email" to email,
        "public_id" to publicId,
        "password" to password
    )
}