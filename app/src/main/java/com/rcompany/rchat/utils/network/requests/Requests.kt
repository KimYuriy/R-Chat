package com.rcompany.rchat.utils.network.requests

import android.content.Context
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject

/**
 * Класс для работы с запросами в сеть
 */
class Requests(private val context: Context) {
    companion object {
        private const val FINGERPRINT_KEY = "fingerprint"
        private const val TOKEN_KEY = "token"
    }

    /**
     * POST-функция отправки POST-запроса
     * @param data данные для отправки типа [Map]. По умолчанию установлено значение [mapOf]
     * @param metadata данные для отправки типа [Map]
     * @param url адрес для отправки запроса типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun post(data: Map<String, String?> = mapOf(), metadata: Map<String, String?>, url: String): ResponseState {
        try {
//          StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val client = OkHttpClient()
            val jsonData = JSONObject(data).toString().toRequestBody("application/json".toMediaTypeOrNull())
            val requestUrl = url.toHttpUrlOrNull()?.toUrl() ?: throw IOException("Bad URL")
            val request = Request.Builder()
                .header("Fingerprint-ID", metadata[FINGERPRINT_KEY]!!)
                .url(requestUrl)
                .post(jsonData)
                .build()
            client.newCall(request).execute().use {
                val code = it.code
                val body = it.body!!.string()
                if (it.isSuccessful) return ResponseState.Success(body)
                return ResponseState.Failure(context, code, body)
            }
        } catch (e: Exception) {
            return ResponseState.Failure(context, -1, e.message.toString())
        }
    }

    /**
     * GET-функция отправки GET-запроса
     * @param metadata метаданные для отправки типа [Map]
     * @param metadata метаданные для отправки типа [Map]
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun get(metadata: Map<String, String?>, url: String, queryParams: Map<String, String>): ResponseState {
        try {
            val client = OkHttpClient()
            val requestUrl = url.toHttpUrlOrNull()?.newBuilder()
                ?.apply { queryParams.forEach { (name, value) -> addQueryParameter(name, value) } }
                ?.build()
                ?.toUrl() ?: throw IOException("Bad url")
            val request = Request.Builder()
                .header("Fingerprint-ID", metadata[FINGERPRINT_KEY]!!)
                .header("Authorization", "Bearer ${metadata[TOKEN_KEY]}")
                .url(requestUrl)
                .get()
                .build()
            client.newCall(request).execute().use {
                val code = it.code
                val body = it.body!!.string()
                if (it.isSuccessful) return ResponseState.Success(body)
                return ResponseState.Failure(context, code, body)
            }
        } catch (e: Exception) {
            return ResponseState.Failure(context, -1, e.message.toString())
        }
    }

    /**
     * PUT-функция отправки PUT-запроса
     * @param requestBody данные для отправки типа [RequestBody]
     * @param metadata метаданные для отправки типа [Map]
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun put(
        requestBody: RequestBody = mapOf<String, String?>().toString().toRequestBody(),
        metadata: Map<String, String?>,
        url: String
    ): ResponseState {
        val client = OkHttpClient()
        val requestUrl = url.toHttpUrlOrNull()?.toUrl()?: throw IOException("Bad URL")
        val request = Request.Builder()
            .header("Fingerprint-ID", metadata[FINGERPRINT_KEY]!!)
            .header("Authorization", "Bearer ${metadata[TOKEN_KEY]}")
            .url(requestUrl)
            .put(requestBody)
            .build()
        client.newCall(request).execute().use {
            val code = it.code
            val body = it.body!!.string()
            if (it.isSuccessful) return ResponseState.Success(body)
            return ResponseState.Failure(context, code, body)
        }
    }
}