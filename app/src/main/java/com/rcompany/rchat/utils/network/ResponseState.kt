package com.rcompany.rchat.utils.network

import android.content.Context
import android.util.Log
import com.rcompany.rchat.R
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

    class Failure(val context: Context, val code: Int, response: String) : ResponseState {
        var errorText: String
        init {
            errorText = when (code) {
                500 -> {
                    Log.e("User", "Server error")
                    context.getString(R.string.internal_server_error_text)
                }
                -1 -> {
                    Log.e("User", "Exception")
                    response
                }
                else -> {
                    Log.e("User", response)
                    getErrorExplanation(context, JSONObject(response)["detail"] as String)
                }
            }
        }

        private fun getErrorExplanation(context: Context, key: String) = mapOf(
            "user_already_exists" to context.getString(R.string.user_already_exists_text),
            "Internal Server Error" to context.getString(R.string.internal_server_error_text),
            "Forbidden" to context.getString(R.string.access_forbidden_text),
            "Unauthorized" to context.getString(R.string.wrong_auth_data_text)
        ).getOrDefault(key, context.getString(R.string.unexpected_error_text))
    }
}