package com.rcompany.rchat.utils.network

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject

/**
 * Класс для работы с запросами в сеть
 */
class Requests private constructor() {
    companion object {

        /**
         * Синхронная POST-функция отправки POST-запроса
         * @param data данные для отправки типа [Map]
         * @return результат запроса типа [String]
         */
        fun post(data: Map<String, String?>, url: String): ResponseState {
            try {
//                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
                val client = OkHttpClient()
                val jsonData = JSONObject(data).toString().toRequestBody("application/json".toMediaTypeOrNull())
                val requestUrl = url.toHttpUrlOrNull()?.toUrl() ?: throw IOException("Bad URL")
                val request = Request.Builder()
                    .header("Fingerprint-ID", "SomeValue") //TODO: Вместо SomeValue вставить цифровой отпечаток устройства
                    .url(requestUrl)
                    .post(jsonData)
                    .build()
                val call = client.newCall(request)
                val response = call.execute()
                response.use {
                    val code = it.code
                    val body = it.body!!.string()
                    if (it.isSuccessful) return ResponseState.Success(body)
                    return ResponseState.Failure(code, body)
                }
            } catch (e: Exception) {
                return ResponseState.Failure(-1, e.message.toString())
            }
        }

        fun get(source: String, url: String): ResponseState {
            try {
                val client = OkHttpClient()

                val requestUrl = url.toHttpUrlOrNull()?.newBuilder()
                    ?.addQueryParameter("match_str", source)
                    ?.build()
                    ?.toUrl() ?: throw IOException("Bad url")

                val request = Request.Builder()
                    .header("Fingerprint-ID", "SomeValue")
                    .header("Authorization", "Bearer my_token")
                    .url(requestUrl)
                    .get()
                    .build()
                val response = client.newCall(request).execute()
                response.use {
                    val code = it.code
                    val body = it.body!!.string()
                    if (it.isSuccessful) return ResponseState.Success(body)
                    return ResponseState.Failure(code, body)
                }
            } catch (e: Exception) {
                return ResponseState.Failure(-1, e.message.toString())
            }

        }
    }
}