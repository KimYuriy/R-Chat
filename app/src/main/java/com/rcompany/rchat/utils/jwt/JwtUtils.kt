package com.rcompany.rchat.utils.jwt

import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.Base64

/**
 * Класс для работы с JWT-токеном
 */
class JwtUtils private constructor() {
    companion object {
        /**
         * Функция парсинга JWT-токена. Разделяет токен на 3 части (в качестве разделителя - точка) и
         * создает массив с ними, с помощью [Base64] декодирует второй элемент массива в строку,
         * преобразует ее в [Map]
         * @param token токен типа [String]
         * @return токен пользователя типа [JSONObject] или null при ошибке парсинга
         */
        fun parseJwtToken(token: String): JSONObject? {
            return try {
                val payload = token.split('.')[1]
                val decodedPayload = String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8)
                JSONObject(decodedPayload)
            } catch (_: Exception) {
                null
            }
        }

        /**
         * Фабричная функция преобразования [JSONObject] в [Map].
         * Итерационно проходит по всему JSON-файлу и добавляет в Map соответствующую запись.
         * @return данные пользователя типа [Map]
         */
        private fun JSONObject.toMap(): Map<String, Any> {
            val map = mutableMapOf<String, Any>()
            val keys = this.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = this.get(key)
                map[key] = value
            }
            return map
        }
    }
}