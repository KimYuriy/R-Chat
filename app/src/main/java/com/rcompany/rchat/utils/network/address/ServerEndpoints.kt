package com.rcompany.rchat.utils.network.address

/**
 * Класс-энумератор эндпоинтов сервера, по которым будут отправляться различные запросы
 */
enum class ServerEndpoints {
    AUTH {
        override val endpoint = "/api/auth"
    },

    SEARCH_USER {
        override val endpoint = "/user/find"
    },

    REGISTER {
        override val endpoint = "/user/create"
    },

    GET_CHATS {
        override val endpoint = "/chat/list"
    },

    GET_MESSAGES {
        override val endpoint = "/message/list"
    },

    USER_PROFILE {
      override val endpoint = "/user/profile"
    },

    CHAT_CREATE {
        override val endpoint = "/chat/create_group"
    },

    GET_CURRENT_USERS {
        override val endpoint = "/chat/get_users"
    },

    ADD_USER_TO_CHAT {
        override val endpoint = "/chat/add_user"
    },

    REMOVE_USER_FROM_CHAT {
        override val endpoint = "/chat/remove_user"
    },

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