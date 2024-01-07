package com.rcompany.rchat.utils.enums

enum class ServerEndpoints {
    AUTH {
        override val endpoint = "auth"
    },
    REGISTER {
        override val endpoint = "register"
    };

    abstract val endpoint: String

    /**
     * Переопределенный метод toString, возвращающий полный адрес сервера с установленным эндпоинтом
     */
    override fun toString() = "https://192.168.91.3/$endpoint"
}