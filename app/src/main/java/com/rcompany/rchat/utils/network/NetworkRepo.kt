package com.rcompany.rchat.utils.network

import android.content.Context
import android.util.Log
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.rcompany.rchat.utils.databases.user.UserDataClass
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.dataclass.UserMetadata
import com.rcompany.rchat.utils.network.requests.Requests
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.utils.network.token.Tokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Класс для работы с сетью
 * @property context контекст приложения типа [Context]
 * @property userRepo репозиторий БД пользователей типа [UserRepo]
 */
class NetworkRepo(private val context: Context, private val userRepo: UserRepo) {

    /**
     * Данные пользователя
     */
    private var userData: UserDataClass? = null

    /**
     * Метаданные пользователя
     */
    private var metadata: UserMetadata

    /**
     * Экземпляр класса [Requests]
     */
    private var requests: Requests

    init {

        /**
         * Получение цифрового отпечатка устройства
         */
        var fingerprint = ""
        FingerprinterFactory.create(context).getFingerprint(version = Fingerprinter.Version.V_5) {
            fingerprint = it
        }

        /**
         * Получение данных пользователя
         */
        userData = userRepo.getUserData()

        /**
         * Установка метаданных пользователя
         */
        metadata = UserMetadata(
            fingerprint,
            if (userData!= null) userData!!.accessToken else null
        )

        /**
         * Установка экземпляра класса Requests
         */
        requests = Requests(context)
    }

    /**
     * POST-запрос на сервер
     * @param url адрес запроса типа [String]
     * @param data данные запроса типа [Map]. По умолчанию [mapOf]
     * @return состояние ответа на запрос типа [ResponseState]
     */
    suspend fun post(url: String, data: Map<String, String?> = mapOf()): ResponseState {
        try {
            if (metadata.token == null) {
                Log.d("NetworkRepo:post", "Token is null")
                return requests.post(data, metadata, url)
            } else {
                Log.d("NetworkRepo:post", "Token is not null: ${metadata.token}")
                return if (isTokenValid(userData!!.accessTokenExp)) {
                    Log.d("NetworkRepo:post", "Token is valid")
                    requests.post(data, metadata, url)
                } else {
                    Log.d("NetworkRepo:post", "Token not valid")
                    if (refreshToken()) {
                        requests.post(data, metadata, url)
                    } else {
                        ResponseState.Failure(context, -1, "Token refresh failed")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NetworkRepo:post", "Exception: ${e.message}")
            return ResponseState.Failure(context, -1, e.message.toString())
        }
    }

    /**
     * GET-запрос на сервер
     * @param url адрес запроса типа [String]
     * @param source данные для запроса типа [String]
     * @return состояние ответа на запрос типа [ResponseState]
     */
    suspend fun get(url: String, source: String): ResponseState {
        try {
            if (metadata.token == null) {
                Log.d("NetworkRepo:get", "Token is null")
                return requests.get(source, metadata, url)
            } else {
                Log.d("NetworkRepo:get", "Token is not null: ${metadata.token}")
                return if (isTokenValid(userData!!.accessTokenExp)) {
                    Log.d("NetworkRepo:get", "Token is valid")
                    requests.get(source, metadata, url)
                } else {
                    Log.d("NetworkRepo:get", "Token not valid")
                    if (refreshToken()) {
                        requests.get(source, metadata, url)
                    } else {
                        ResponseState.Failure(context, -1, "Token refresh failed")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NetworkRepo:get", "Exception: ${e.message}")
            return ResponseState.Failure(context, -1, e.message.toString())
        }
    }

    /**
     * Функция проверки валидности access_token
     * @param expTime время жизни access_token в секундах типа [Long]
     * @return результат проверки типа [Boolean]
     */
    private fun isTokenValid(expTime: Long): Boolean {
        val currentTimeUnix = System.currentTimeMillis()
        return currentTimeUnix < expTime
    }

    /**
     * Функция обновления access_token
     * @return результат обновления типа [Boolean]
     */
    private suspend fun refreshToken(): Boolean {
        val prevToken = metadata.token
        metadata.token = userData!!.refreshToken
        try {
            return CoroutineScope(Dispatchers.IO).async {
                when (val state = requests.put(
                    metadata = metadata,
                    url = ServerEndpoints.REFRESH_TOKEN.toString())
                ) {
                    is ResponseState.Success -> {
                        Log.d("NetworkRepo:refreshToken", "Refreshing token successful")
                        Log.d("NetworkRepo:refreshToken", "New Tokens: ${state.data}")
                        val tokens = Tokens(state.data).parseToken(userRepo.getUserData()!!.publicId)
                        withContext(Dispatchers.Main) {
                            userRepo.saveUserData(tokens)
                            userData = userRepo.getUserData()
                            Log.d("NetworkRepo:refreshToken:withContext Main", "Access token: ${userData!!.accessToken}")
                            metadata.token = userData!!.accessToken
                            true
                        }
                    }
                    is ResponseState.Failure -> {
                        Log.e("NetworkRepo:refreshToken", "Refreshing token failed")
                        metadata.token = prevToken
                        false
                    }
                }
            }.await()
        } catch (e: Exception) {
            Log.e("NetworkRepo:refreshToken", "Exception: ${e.message}")
            return false
        }
    }

}