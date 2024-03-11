package com.rcompany.rchat.utils.network.socket

import android.util.Log
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.network.address.ServerAddress
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

/**
 * Класс для работы с сокетом
 */
class Websocket(private val chatsRepo: ChatsRepo) {
    companion object {

        /**
         * Переменная-экземпляр сокета
         */
        @Volatile
        private var instance: Websocket? = null

        /**
         * Функция возвращения экземпляра класса
         * @return экземпляр класса типа [Websocket]
         */
        fun getInstance(chatsRepo: ChatsRepo) =
            instance ?: synchronized(this) {
                instance ?: Websocket(chatsRepo).also {
                    instance = it
                }
            }
    }

    /**
     * Переменная сокета
     */
    private var socket: Socket? = null

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
        val options = IO.Options().apply {
            reconnection = true
            path = "/socks"
            transports = arrayOf("websocket")
        }
        socket = IO.socket("${ServerAddress.value}/", options)
        socket!!.connect()
        if (socket!!.connected()) Log.d("Websocket:openConnection", "Connected")

        socket!!.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("Websocket:openConnection", "Connect error: ${it[0]}")
        }

        socket!!.on("message") { args ->
            Log.d("Websocket:openConnection", args.toString())
            chatsRepo.receiveMessage(args[0] as JSONObject)
        }
    }

    /**
     * Функция закрытия соединения
     */
    fun closeConnection() {
        socket!!.close()
        if (socket!!.connected()) Log.d("Websocket:closeConnection", "Disconnected")
    }

    /**
     * Функция отправки сообщения
     */
    fun send(messageData: String) {
        Log.d("Websocket:send", messageData)
        socket!!.emit("message", messageData)
    }
}