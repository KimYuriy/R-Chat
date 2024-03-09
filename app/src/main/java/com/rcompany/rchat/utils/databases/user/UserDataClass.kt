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
        fun fromJson(json: JSONObject): UserDataClass {
            return UserDataClass(
                json["public_id"] as String,
                json["access_token"] as String,
                json["refresh_token"] as String,
                (json["access_token_exp"] as Int).toLong()
            )
        }
    }

    fun toJson(): JSONObject {
        return JSONObject()
            .put("public_id", publicId)
            .put("access_token", accessToken)
            .put("refresh_token", refreshToken)
            .put("access_token_exp", accessTokenExp)
    }
}