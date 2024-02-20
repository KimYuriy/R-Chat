package com.rcompany.rchat.utils.network

import android.content.Context
import com.rcompany.rchat.utils.network.address.dataclass.UserMetadata
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
class Requests(private val context: Context) {

    /**
     * Синхронная POST-функция отправки POST-запроса
     * @param data данные для отправки типа [Map]
     * @return результат запроса типа [String]
     */
    fun post(data: Map<String, String?>, metadata: UserMetadata, url: String): ResponseState {
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

    fun get(source: String, metadata: UserMetadata, url: String): ResponseState {
        try {
            val client = OkHttpClient()
            val requestUrl = url.toHttpUrlOrNull()?.newBuilder()
                ?.addQueryParameter("match_str", source)
                ?.build()
                ?.toUrl() ?: throw IOException("Bad url")
            val request = Request.Builder()
                .header("Fingerprint-ID", metadata.deviceFingerprint)
                .header("Authorization", "Bearer ${metadata.userToken}")
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
}