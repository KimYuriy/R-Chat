package com.rcompany.rchat.utils.network.address

import com.rcompany.rchat.utils.network.address.ServerAddress

/**
 * Класс-энумератор эндпоинтов сервера, по которым будут отправляться различные запросы
 */
enum class ServerEndpoints {
    /**
     * Эндпоинт авторизации
     */
    AUTH {
        override val endpoint = "/api/auth"
    },

    /**
     * Эндпоинт получения списка пользователей
     */
    SEARCH_USER {
        override val endpoint = "/user/find"
    },

    /**
     * Эндпоинт регистрации
     */
    REGISTER {
        override val endpoint = "/user/create"
    },

    /**
     * Эндпоинт обновления токена
     */
    REFRESH_TOKEN {
        override val endpoint = "/api/refresh_tokens"
    };

    /**
     * Абстрактный эндпоинт, перезаписываемый для каждого эндпоинта
     */
    abstract val endpoint: String

    /**
     * Адрес сервера
     */
    private val serverAddress = ServerAddress.value

    /**
     * Переопределенный метод toString, возвращающий полный адрес сервера с установленным эндпоинтом
     */
    override fun toString() = "$serverAddress$endpoint"
}