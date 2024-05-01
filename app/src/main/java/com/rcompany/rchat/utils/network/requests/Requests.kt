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
    fun post(data: Map<String, Any?> = mapOf(), url: String, headers: Map<String, String>): ResponseState {
        try {
            val client = OkHttpClient()
            val jsonData = JSONObject(data).toString().toRequestBody("application/json".toMediaTypeOrNull())
            val requestUrl = url.toHttpUrlOrNull()?.toUrl() ?: throw IOException("Bad URL")
            val request = Request.Builder()
                .apply { headers.forEach { (name, value) -> addHeader(name, value) } }
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
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun get(url: String, queryParams: Map<String, Any>, headers: Map<String, String>): ResponseState {
        try {
            val client = OkHttpClient()
            val requestUrl = url.toHttpUrlOrNull()?.newBuilder()
                ?.apply { queryParams.forEach { (name, value) -> addQueryParameter(name, value.toString()) } }
                ?.build()
                ?.toUrl() ?: throw IOException("Bad url")
            val request = Request.Builder()
                .apply { headers.forEach { (name, value) -> header(name, value) } }
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
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun put(
        requestBody: RequestBody = mapOf<String, String?>().toString().toRequestBody(),
        url: String,
        headers: Map<String, String>
    ): ResponseState {
        val client = OkHttpClient()
        val requestUrl = url.toHttpUrlOrNull()?.toUrl()?: throw IOException("Bad URL")
        val request = Request.Builder()
            .apply { headers.forEach { (name, value) -> addHeader(name, value) } }
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