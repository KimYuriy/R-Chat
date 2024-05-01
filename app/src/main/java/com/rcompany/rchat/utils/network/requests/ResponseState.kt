package com.rcompany.rchat.utils.network.requests

import android.content.Context
import android.util.Log
import com.rcompany.rchat.R
import org.json.JSONObject

/**
 * Интерфейс ответа от сервера
 */
sealed interface ResponseState {

    /**
     * Класс успешного ответа от сервера
     * @param response ответ от сервера типа [String]
     */
    class Success(response: String) : ResponseState {

        /**
         * Поле полученных данных.
         * Пустой JSON лежит в дате, если пустое тело ответа
         */
        var data = if (response != "null") {
            JSONObject(response)
        } else {
            JSONObject()
        }
    }

    /**
     * Класс ошибки ответа от сервера
     * @param context контекст приложения типа [Context]
     * @param code код ошибки типа [Int]
     * @param response ответ от сервера типа [String]
     */
    class Failure(val context: Context, val code: Int, response: String) : ResponseState {

        /**
         * Текст ошибки для дальнейшего отображения
         */
        var errorText: String
        init {
            /**
             * Установка текста ошибки
             */
            errorText = when (code) {
                500 -> context.getString(R.string.internal_server_error_text)
                -1 -> response
                422 -> JSONObject(response).toString(2)
                else -> getErrorExplanation(context, JSONObject(response)["detail"] as String)
            }
            Log.e("ResponseState:Failure", "Code: $code, error: $errorText")
        }

        /**
         * Функция получения текста ошибки из текста, содержащегося в ответе от сервера
         * @return текст ошибки типа [String]
         */
        private fun getErrorExplanation(context: Context, key: String) = mapOf(
            "user_already_exists" to context.getString(R.string.user_already_exists_text),
            "Internal Server Error" to context.getString(R.string.internal_server_error_text),
            "Forbidden" to context.getString(R.string.access_forbidden_text),
            "Unauthorized" to context.getString(R.string.wrong_auth_data_text)
        ).getOrDefault(key, context.getString(R.string.unexpected_error_text))
    }
}