package com.rcompany.rchat.utils.network

import android.content.Context
import android.util.Log
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.rcompany.rchat.utils.databases.user.dataclasses.UserMetaDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.extensions.isInternetAvailable
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.Requests
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.utils.network.socket.Websocket
import com.rcompany.rchat.utils.network.token.Tokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId

/**
 * Класс для работы с сетью
 * @property context контекст приложения типа [Context]
 * @property userRepo репозиторий БД пользователей типа [UserRepo]
 */
class NetworkManager(val context: Context, private val userRepo: UserRepo) {
    companion object {
        private const val FINGERPRINT_KEY = "fingerprint"
        private const val TOKEN_KEY = "token"
    }

    private  val websocket = Websocket()

    /**
     * Данные пользователя
     */
    var userData: UserMetaDataClass? = null

    /**
     * Метаданные пользователя
     */
    private var userMetadata: MutableMap<String, String?>

    /**
     * Экземпляр класса [Requests]
     */
    private var requests: Requests

    init {

        var fingerprint = ""
        FingerprinterFactory.create(context).getFingerprint(version = Fingerprinter.Version.V_5) {
            fingerprint = it
        }

        userData = userRepo.getUserMetaData()

        userMetadata = mutableMapOf(
            FINGERPRINT_KEY to fingerprint,
            TOKEN_KEY to if (userData != null) userData!!.accessToken else null
        )
        Log.w("NetworkManager:init", "User metadata: $userMetadata")
        requests = Requests(context)
    }

    /**
     * POST-запрос на сервер
     * @param url адрес запроса типа [String]
     * @param data данные запроса типа [Map]. По умолчанию [mapOf]
     * @return состояние ответа на запрос типа [ResponseState]
     */
    suspend fun post(url: String, data: Map<String, Any?> = mapOf(), hasAuthToken: Boolean): ResponseState {
        if (isInternetAvailable(context)) {
            val headers = if (hasAuthToken) {
                mapOf(
                    "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                    "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
                )
            } else {
                mapOf("Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!)
            }
            try {
                if (userMetadata[TOKEN_KEY] == null) {
                    Log.d("NetworkManager:post", "Token is null")
                    return requests.post(data, url, headers)
                } else {
                    Log.d("NetworkManager:post", "Token is not null: ${userMetadata[TOKEN_KEY]}")
                    return if (isTokenValid(userData!!.accessTokenExp)) {
                        Log.d("NetworkManager:post", "Token is valid")
                        requests.post(data, url, headers)
                    } else {
                        Log.d("NetworkManager:post", "Token not valid")
                        if (refreshToken()) {
                            requests.post(data, url, headers)
                        } else {
                            ResponseState.Failure(context, -1, "Token refresh failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkManager:post", "Exception: ${e.message}")
                return ResponseState.Failure(context, -1, e.message.toString())
            }
        } else {
            return ResponseState.Failure(context, -1, "No internet connection")
        }
    }

    /**
     * GET-запрос на сервер
     * @param url адрес запроса типа [String]
     * @param queryParams параметры запроса типа [Map] (по умолчанию [mapOf])
     * @return состояние ответа на запрос типа [ResponseState]
     */
    suspend fun get(url: String, queryParams: Map<String, Any> = mapOf()): ResponseState {
        if (isInternetAvailable(context)) {
            var headers = mapOf(
                "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
            )
            Log.w("NetworkManager:get", "Headers: $headers")
            try {
                if (userMetadata[TOKEN_KEY] == null) {
                    Log.d("NetworkManager:get", "Token is null")
                    return requests.get(url, queryParams, headers)
                } else {
                    Log.d("NetworkManager:get", "Token is not null: ${userMetadata[TOKEN_KEY]}")
                    return if (isTokenValid(userData!!.accessTokenExp)) {
                        Log.d("NetworkManager:get", "Token is valid")
                        requests.get(url, queryParams, headers)
                    } else {
                        Log.d("NetworkManager:get", "Token not valid")
                        if (refreshToken()) {
                            headers = mapOf(
                                "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                                "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
                            )
                            requests.get(url, queryParams, headers)
                        } else {
                            ResponseState.Failure(context, -1, "Token refresh failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkManager:get", "Exception: ${e.message}")
                return ResponseState.Failure(context, -1, e.message.toString())
            }
        } else {
            return ResponseState.Failure(context, -1, "No internet connection")
        }
    }

    suspend fun put(url: String, query: Map<String, String?>): ResponseState {
        if (isInternetAvailable(context)) {
            var headers = mapOf(
                "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
            )
            try {
                val json = JSONObject(query).toString()
                Log.w("NetworkManager:put", json)
                if (userMetadata[TOKEN_KEY] == null) {
                    Log.d("NetworkManager:put", "Token is null")
                    return requests.put(json.toRequestBody(), url, headers)
                } else {
                    Log.d("NetworkManager:put", "Token is not null")
                    return if (isTokenValid(userData!!.accessTokenExp)) {
                        Log.d("NetworkManager:put", "Token is valid")
                        requests.put(json.toRequestBody(), url, headers)
                    } else {
                        Log.d("NetworkManager:put", "Token not valid")
                        if (refreshToken()) {
                            headers = mapOf(
                                "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                                "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
                            )
                            requests.put(json.toRequestBody(), url, headers)
                        } else {
                            ResponseState.Failure(context, -1, "Token refresh failed")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkManager:put", "Exception: ${e.message}")
                return ResponseState.Failure(context, -1, e.message.toString())
            }
        } else {
            return ResponseState.Failure(context, -1, "No internet connection")
        }
    }

    /**
     * Функция проверки валидности access_token
     * @param expTime время жизни access_token в секундах типа [Long]
     * @return результат проверки типа [Boolean]
     */
//    private fun isTokenValid(expTime: Long): Boolean {
//        val currentTimeUnix = System.currentTimeMillis() / 1000
//        return currentTimeUnix < expTime
//    }
    private fun isTokenValid(expTime: Long): Boolean {
        val moscowZoneId = ZoneId.of("Europe/Moscow")
        val moscowCurrentTime = Instant.now().atZone(moscowZoneId).toEpochSecond()
        return moscowCurrentTime < expTime
    }

    /**
     * Функция обновления access_token
     * @return результат обновления типа [Boolean]
     */
    private suspend fun refreshToken(): Boolean {
        if (isInternetAvailable(context)) {
            val prevToken = userMetadata[TOKEN_KEY]
            userMetadata[TOKEN_KEY] = userData!!.refreshToken
            val headers = mapOf(
                "Fingerprint-ID" to userMetadata[FINGERPRINT_KEY]!!,
                "Authorization" to "Bearer ${userMetadata[TOKEN_KEY]}"
            )
            try {
                return CoroutineScope(Dispatchers.IO).async {
                    when (val state = requests.put(
                        url = ServerEndpoints.REFRESH_TOKEN.toString(),
                        headers = headers
                    )) {
                        is ResponseState.Success -> {
                            Log.d("NetworkManager:refreshToken", "Refreshing token successful")
                            Log.d("NetworkManager:refreshToken", "New Tokens: ${state.data}")
                            val tokens = Tokens(state.data).parseToken(userRepo.getUserMetaData()!!.publicId)
                            withContext(Dispatchers.Main) {
                                userRepo.saveUserMetaData(tokens)
                                userData = userRepo.getUserMetaData()
                                Log.d("NetworkManager:refreshToken:withContext Main", "Access token: ${userData!!.accessToken}")
                                userMetadata[TOKEN_KEY] = userData!!.accessToken
                                true
                            }
                        }
                        is ResponseState.Failure -> {
                            Log.e("NetworkManager:refreshToken", "Refreshing token failed")
                            userMetadata[TOKEN_KEY] = prevToken
                            false
                        }
                    }
                }.await()
            } catch (e: Exception) {
                Log.e("NetworkManager:refreshToken", "Exception: ${e.message}")
                return false
            }
        } else {
            return false
        }
    }

    suspend fun openConnection() {
        if (!isTokenValid(userData!!.accessTokenExp)) {
            refreshToken()
        }
        websocket.openConnection(
            mapOf(
                "Fingerprint-ID" to listOf(userMetadata[FINGERPRINT_KEY]!!),
                "Authorization" to listOf("Bearer ${userMetadata[TOKEN_KEY]!!}")
            )
        )
    }

    fun listenEvents(
        parseNewMessage: (JSONObject) -> Unit,
        editMessage: (JSONObject) -> Unit,
        deleteMessage: (JSONObject) -> Unit,
        readMessage: (JSONObject) -> Unit,
        addedInChat: (JSONObject) -> Unit
    ) = websocket.listenEvents(parseNewMessage, editMessage, deleteMessage, readMessage, addedInChat)

    fun closeConnection() = websocket.closeConnection()

    fun sendMessage(message: JSONObject) = websocket.sendMessage(message)

    fun editMessage(message: JSONObject) = websocket.editMessage(message)

    fun deleteMessage(message: JSONObject) = websocket.deleteMessage(message)

    fun readMessage(message: JSONObject) = websocket.readMessage(message)
}