package com.rcompany.rchat.utils.enums

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
        override val endpoint = "register"
    };

    abstract val endpoint: String

    /**
     * Переопределенный метод toString, возвращающий полный адрес сервера с установленным эндпоинтом
     */
    override fun toString() = "https://192.168.91.3/$endpoint"
}