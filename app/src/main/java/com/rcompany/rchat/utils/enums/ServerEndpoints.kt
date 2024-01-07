package com.rcompany.rchat.utils.enums

enum class ServerEndpoints {
    AUTH {
        override val endpoint = "/auth"
    },
    REGISTER {
        override val endpoint = "/register"
    };

    abstract val endpoint: String

    override fun toString() = endpoint
}