package com.rcompany.rchat.utils.network.requests

import android.content.Context
import com.rcompany.rchat.utils.network.dataclass.UserMetadata
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

    /**
     * POST-функция отправки POST-запроса
     * @param data данные для отправки типа [Map]. По умолчанию установлено значение [mapOf]
     * @param metadata данные для отправки типа [UserMetadata]
     * @param url адрес для отправки запроса типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun post(data: Map<String, String?> = mapOf(), metadata: UserMetadata, url: String): ResponseState {
        try {
//          StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val client = OkHttpClient()
            val jsonData = JSONObject(data).toString().toRequestBody("application/json".toMediaTypeOrNull())
            val requestUrl = url.toHttpUrlOrNull()?.toUrl() ?: throw IOException("Bad URL")
            val request = Request.Builder()
                .header("Fingerprint-ID", metadata.deviceFingerprint)
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
     * @param source данные для отправки типа [String]
     * @param metadata метаданные для отправки типа [UserMetadata]
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun get(source: String, metadata: UserMetadata, url: String): ResponseState {
        try {
            val client = OkHttpClient()
            val requestUrl = url.toHttpUrlOrNull()?.newBuilder()
                ?.addQueryParameter("match_str", source)
                ?.build()
                ?.toUrl() ?: throw IOException("Bad url")
            val request = Request.Builder()
                .header("Fingerprint-ID", metadata.deviceFingerprint)
                .header("Authorization", "Bearer ${metadata.token}")
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
     * @param metadata метаданные для отправки типа [UserMetadata]
     * @param url адрес для отправки типа [String]
     * @return состояние ответа типа [ResponseState]
     */
    fun put(
        requestBody: RequestBody = mapOf<String, String?>().toString().toRequestBody(),
        metadata: UserMetadata,
        url: String
    ): ResponseState {
        val client = OkHttpClient()
        val requestUrl = url.toHttpUrlOrNull()?.toUrl()?: throw IOException("Bad URL")
        val request = Request.Builder()
            .header("Fingerprint-ID", metadata.deviceFingerprint)
            .header("Authorization", "Bearer ${metadata.token}")
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