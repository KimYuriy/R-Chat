package com.rcompany.rchat.utils.network.socket

import android.util.Log
import com.rcompany.rchat.utils.network.address.ServerAddress
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Класс для работы с сокетом
 */
class Websocket {
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
    private var socket: Socket? = null

    /**
     * Функция открытия соединения
     */
    fun openConnection(headers: Map<String, List<String>> = mapOf()) {
        val options = IO.Options().apply {
            reconnection = true
            path = "/socks"
            transports = arrayOf("websocket")
            extraHeaders = headers
        }
        socket = IO.socket("${ServerAddress.value}/", options)
        socket!!.connect()
        if (socket!!.connected()) Log.d("Websocket:openConnection", "Connected")
        else Log.e("Websocket:openConnection", "Connection error")
    }

    fun listenEvents(
        parseNewMessage: (JSONObject) -> Unit,
        editMessage: (JSONObject) -> Unit,
        deleteMessage: (JSONObject) -> Unit,
        readMessage: (JSONObject) -> Unit,
        addedInChat: (JSONObject) -> Unit
    ) {
        socket!!.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("Websocket:openConnection", "Connect error: ${it[0]}")
        }

        GlobalScope.launch(Dispatchers.IO) {
            socket!!.on("_new_message_") { args -> parseNewMessage(JSONObject(args[0] as String)) }

            socket!!.on("_edit_message_") { args -> editMessage(JSONObject(args[0] as String)) }

            socket!!.on("_delete_message_") { args -> deleteMessage(JSONObject(args[0] as String)) }

            socket!!.on("_read_message_") { args -> readMessage(JSONObject(args[0] as String)) }

            socket!!.on("_added_in_chat_") { args -> addedInChat(JSONObject(args[0] as String))}
        }
    }

    /**
     * Функция закрытия соединения
     */
    fun closeConnection() {
        socket!!.close()
        if (socket!!.connected()) Log.w("Websocket:closeConnection", "Disconnected")
    }

    /**
     * Функция отправки сообщения
     */
    fun sendMessage(messageData: JSONObject) {
        Log.d("Websocket:send", messageData.toString())
        socket!!.emit("_new_message_", messageData)
    }

    fun editMessage(messageData: JSONObject) {
        Log.d("Websocket:edit", messageData.toString())
        socket!!.emit("_edit_message_", messageData)
    }

    fun deleteMessage(messageData: JSONObject) {
        Log.d("Websocket:delete", messageData.toString())
        socket!!.emit("_delete_message_", messageData)
    }

    fun readMessage(messageData: JSONObject) {
        Log.d("Websocket:read", messageData.toString())
        socket!!.emit("_read_message_", messageData)
    }
}