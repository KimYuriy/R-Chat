package com.rcompany.rchat.utils.databases.user

import org.json.JSONObject

/**
 * Дата-класс сохраняемых данных пользователя
 * @property publicId идентификатор пользователя типа [Int]
 * @property accessToken access token пользователя типа [String]
 * @property refreshToken refresh token пользователя типа [String]
 * @property accessTokenExp время жизни access token пользователя типа [Long]
 */
data class UserDataClass(
    val publicId: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExp: Long
) {
    companion object {
        private const val PUBLIC_ID_KEY = "public_id"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val ACCESS_TOKEN_EXP_KEY = "access_token_exp"

        fun fromJson(json: JSONObject): UserDataClass {
            return UserDataClass(
                json[PUBLIC_ID_KEY] as String,
                json[ACCESS_TOKEN_KEY] as String,
                json[REFRESH_TOKEN_KEY] as String,
                (json[ACCESS_TOKEN_EXP_KEY] as Int).toLong()
            )
        }
    }

    fun toJson(): JSONObject {
        return JSONObject()
            .put(PUBLIC_ID_KEY, publicId)
            .put(ACCESS_TOKEN_KEY, accessToken)
            .put(REFRESH_TOKEN_KEY, refreshToken)
            .put(ACCESS_TOKEN_EXP_KEY, accessTokenExp)
    }
}