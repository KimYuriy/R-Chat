package com.rcompany.rchat.utils.network.socket

import android.util.Log
import com.rcompany.rchat.utils.network.address.ServerAddress
import io.socket.client.IO

/**
 * Класс для работы с сокетом
 */
class Websocket {
    companion object {

        /**
         * Переменная-инстанс сокета
         */
        @Volatile
        private var instance: Websocket? = null

        /**
         * Функция возвращения экземпляра класса
         * @return экземпляр класса типа [Websocket]
         */
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Websocket().also {
                    instance = it
                }
            }
    }

    /**
     * Переменная сокета
     */
    private val socket = IO.socket("ws://${ServerAddress.value}")

    /**
     * Конструктор класса
     */
    init {
        openConnection()
    }

    /**
     * Функция открытия соединения
     */
    private fun openConnection() {
        socket.connect()
        if (socket.connected()) Log.d("Websocket:openConnection", "Connected")
    }

    /**
     * Функция закрытия соединения
     */
    fun closeConnection() {
        socket.close()
        if (socket.connected()) Log.d("Websocket:closeConnection", "Disconnected")
    }

    /**
     * Функция отправки сообщения
     */
    fun sendMessage(messageData: String) {
        socket.send()
    }
}