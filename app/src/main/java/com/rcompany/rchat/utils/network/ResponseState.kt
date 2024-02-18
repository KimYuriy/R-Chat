package com.rcompany.rchat.utils.network

import android.util.Log
import org.json.JSONObject

sealed interface ResponseState {
    class Success(response: String) : ResponseState {
        // Пустой JSON лежит в дате, если пустое тело ответа
        var data = if (response != "null") {
            JSONObject(response)
        } else {
            JSONObject()
        }
    }

    class Failure(val code: Int, response: String) : ResponseState {
        var errorText: String
        init {
            errorText = when (code) {
                500 -> {
                    Log.e("User", "Server error")
                    "Внутренняя ошибка сервера"
                }
                -1 -> {
                    Log.e("User", "Exception")
                    response
                }
                else -> {
                    Log.e("User", response)
                    getErrorExplanation(JSONObject(response)["detail"] as String)
                }
            }
        }

        private fun getErrorExplanation(key: String) = mapOf(
            "user_already_exists" to "Пользователь с такими данными уже существует",
            "Internal Server Error" to "Ошибка сервера",
            "Forbidden" to "Доступ к ресурсу запрещен",
            "Unauthorized" to "Введены неверные данные для авторизации"
        ).getOrDefault(key, "Возникла непредвиденная ошибка")
    }
}