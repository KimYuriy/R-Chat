package com.rcompany.rchat.utils.network.token

import android.util.Log
import com.rcompany.rchat.utils.databases.user.UserDataClass
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.Base64

/**
 * Класс для работы с токеном
 * @property token токен типа [JSONObject]
 */
class Tokens(private val token: JSONObject) {

    /**
     * Функция получения данных пользователя по токену
     * @return данные пользователя типа [UserDataClass]
     */
    fun parseToken(public_id: String? = null): UserDataClass {
        Log.d("Tokens:parseToken", token.toString())
        val encodedAccessToken = token["access_token"] as String
        val encodedRefreshToken = token["refresh_token"] as String
        val decodedAccessToken = parseJwtToken(encodedAccessToken)
        val publicId = public_id ?: decodedAccessToken!!["public_id"] as String
        val accessTokenLifetime = (decodedAccessToken!!["exp"] as Int).toLong()
        Log.d("Tokens:parseToken", "Public id: $publicId")
        Log.d("Tokens:parseToken", "Encoded access token: $encodedAccessToken")
        Log.d("Tokens:parseToken", "Encoded refresh token: $encodedRefreshToken")
        Log.d("Tokens:parseToken", "Access token lifetime: $accessTokenLifetime")
        return UserDataClass(
            publicId,
            encodedAccessToken,
            encodedRefreshToken,
            accessTokenLifetime
        )
    }

    /**
     * Функция парсинга токена из [token]
     * @param token токен типа [String]
     * @return токен типа null-[JSONObject]
     */
    private fun parseJwtToken(token: String) = try {
            val payload = token.split('.')[1]
            val decodedPayload = String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8)
            JSONObject(decodedPayload)
        } catch (_: Exception) {
            null
        }
}