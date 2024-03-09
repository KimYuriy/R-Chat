package com.rcompany.rchat.utils.network.dataclass

/**
 * Дата-класс метаданных пользователя
 * @property deviceFingerprint цифровой отпечаток устройства типа [String]
 * @property token токен пользователя типа [String]
 */
data class UserMetadata(
    var deviceFingerprint: String,
    var token: String? = null
)