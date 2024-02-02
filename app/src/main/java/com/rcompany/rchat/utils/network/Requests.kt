package com.rcompany.rchat.utils.network

import android.os.StrictMode
import android.util.Log
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
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
        fun post(data: Map<String, String?>, url: String): JSONObject {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val client = OkHttpClient()
            val jsonData = JSONObject(data).toString().toRequestBody("application/json".toMediaTypeOrNull())
            val requestUrl = url.toHttpUrlOrNull()?.toUrl() ?: throw IOException("Bad URL")
            val request = Request.Builder()
                .url(requestUrl)
                .post(jsonData)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            response.use {
                val body = it.body!!.string()
                val code = it.code
                if (!it.isSuccessful) {
                    Log.e("User_network", "$code")
                }
                return JSONObject(body)
            }
        }

//        fun get(data: Map<String, String>, url: String): String {
//            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
//            val httpBuilder = url.toHttpUrlOrNull() ?: throw IOException("Bad url")
//            val queryData = httpBuilder.newBuilder()
//            data.forEach { (key, value) ->
//                queryData.addQueryParameter(key, value)
//            }
//            val request1 = Request.Builder()
//                .get()
//                .url(queryData.build())
//                .build()
//            client.newCall(request1).execute().use { response ->
//                if (!response.isSuccessful) throw IOException("${response.code}")
//                return response.body!!.string()
//            }
//        }
    }
}