package com.rcompany.rchat.utils.databases.window_dataclasses

/**
 * Дата-класс данных, введенных при регистрации
 * @property email почта пользователя типа [String]
 * @property login логин пользователя типа [String]
 * @property publicId уникальный ID пользователя типа [String]
 * @property password пароль пользователя типа [String]
 */
data class RegisterDataClass(
    var email: String,
    var publicId: String,
    var password: String
) {

    /**
     * Функция получения [Map] из данного класса
     * @return данные в формате [Map]
     */
    fun toMap() = mapOf("email" to email, "password" to password, "public_id" to publicId)
}