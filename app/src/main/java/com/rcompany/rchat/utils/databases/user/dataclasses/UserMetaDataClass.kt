package com.rcompany.rchat.utils.databases.user.dataclasses

import org.json.JSONObject

/**
 * Дата-класс сохраняемых данных пользователя
 * @property publicId идентификатор пользователя типа [Int]
 * @property accessToken access token пользователя типа [String]
 * @property refreshToken refresh token пользователя типа [String]
 * @property accessTokenExp время жизни access token пользователя типа [Long]
 */
data class UserMetaDataClass(
    val publicId: String,
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExp: Long
) {
    companion object {
        private const val PUBLIC_ID_KEY = "public_id"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val ACCESS_TOKEN_EXP_KEY = "access_token_exp"
        private const val USER_ID_KEY = "user_id"

        fun fromJson(json: JSONObject): UserMetaDataClass {
            return UserMetaDataClass(
                json.getString(PUBLIC_ID_KEY),
                json.getString(USER_ID_KEY),
                json.getString(ACCESS_TOKEN_KEY),
                json.getString(REFRESH_TOKEN_KEY),
                json.getLong(ACCESS_TOKEN_EXP_KEY)
            )
        }
    }

    fun toJson(): JSONObject {
        return JSONObject()
            .put(PUBLIC_ID_KEY, publicId)
            .put(USER_ID_KEY, userId)
            .put(ACCESS_TOKEN_KEY, accessToken)
            .put(REFRESH_TOKEN_KEY, refreshToken)
            .put(ACCESS_TOKEN_EXP_KEY, accessTokenExp)
    }
}