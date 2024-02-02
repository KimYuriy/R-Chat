package com.rcompany.rchat.utils.enums

import com.rcompany.rchat.utils.network.address.ServerAddress

/**
 * Класс-энумератор эндпоинтов сервера, по которым будут отправляться различные запросы
 */
enum class ServerEndpoints {
    /**
     * Эндпоинт авторизации
     */
    AUTH {
        override val endpoint = "authorization"
    },

    /**
     * Эндпоинт регистрации
     */
    REGISTER {
        override val endpoint = "/user/create"
    };

    abstract val endpoint: String

    private val serverAddress = ServerAddress.testValue
    /**
     * Переопределенный метод toString, возвращающий полный адрес сервера с установленным эндпоинтом
     */

    override fun toString() = "$serverAddress$endpoint"
}