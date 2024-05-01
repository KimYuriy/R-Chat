package com.rcompany.rchat.windows.messages.viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.R
import com.rcompany.rchat.databinding.AddUserToChatAlertBinding
import com.rcompany.rchat.databinding.MessageActionsAlertBinding
import com.rcompany.rchat.utils.JasonStatham
import com.rcompany.rchat.utils.databases.chats.ChatsRepo
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.CurrentChatDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.outgoing.NewMessageDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.windows.create_group_chat.dataclasses.SelectedUserDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Класс-контроллер управления состоянием MessagesWindow типа [ViewModel]
 * @property chatsRepo репозиторий чатов типа [ChatsRepo]
 * @property userRepo репозиторий пользователей типа [UserRepo]
 */
class MessagesViewModel(
    private val currentChatData: CurrentChatDataClass?,
    private val otherUserPublicID: String?,
    private val chatsRepo: ChatsRepo,
    private val userRepo: UserRepo,
): ViewModel() {
    init {
        Log.d("MessagesViewModel:init", "init")
        if (currentChatData != null) {
            chatsRepo.filterMessagesByChatData(currentChatData)
            chatsRepo.requestForAllMessages()
        }
    }

    val messages = chatsRepo.getMessages()
    private var currentUsers = ArrayList<SelectedUserDataClass>()
    private var foundUsers = ArrayList<SelectedUserDataClass>()
    val currentUsersLiveData = MutableLiveData<ArrayList<SelectedUserDataClass>>()
    val foundUsersLiveData = MutableLiveData<ArrayList<SelectedUserDataClass>>()

    /**
     * Функция получения данных пользователя
     * @return данные пользователя типа
     */
    fun getUserData() = userRepo.getUserMetaData()

    fun sendMessage(message: String, chatType: String?) {
        val messageData = if (currentChatData == null) {
            NewMessageDataClass(
                other_user_public_id = otherUserPublicID,
                message_text = message,
                is_work_chat = chatType == ChatTypes.WORK.value
            )
        } else {
            NewMessageDataClass(
                chat_id = currentChatData.id,
                message_text = message
            )
        }.toJson()
        chatsRepo.sendMessage(messageData)
    }

    fun showGroupChatPopupMenu(context: Context, view: View) {
        val menu = PopupMenu(context, view)
        menu.menuInflater.inflate(R.menu.group_chat_settings_menu, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.showUsers -> {
                    true
                }
                R.id.editInfo -> {
                    true
                }
                R.id.leaveChat -> {
                    showLeaveChatAlertDialog(context)
                    true
                }
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }
        menu.show()
    }

    private fun showLeaveChatAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Leave chat")
        builder.setMessage("Are you sure you want to leave this chat?")
        builder.setPositiveButton("Yes") { _, _ ->
            //todo: leave chat
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    fun showPrivateChatPopupMenu(context: Context, view: View) {
        val menu = PopupMenu(context, view)
        menu.menuInflater.inflate(R.menu.private_chat_settings_menu, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }
        menu.show()
    }

    fun isCurrentTimeBetween(startTime: String, endTime: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val start = LocalTime.parse(startTime, formatter)
        val end = LocalTime.parse(endTime, formatter)
        val now = LocalTime.now()
        Log.w("MessagesViewModel:isCurrentTimeBetween", "Start time: $start; End time: $end; Current time: $now")
        return if (start <= end) {
            // Случай, когда время начала и окончания находятся в одних сутках
            now.isAfter(start) && now.isBefore(end)
        } else {
            // Случай, когда интервал времени пересекает полночь
            now.isAfter(start) || now.isBefore(end)
        }
    }

    fun getCurrentUsers(context: Context, chatId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(context, userRepo).get(
                ServerEndpoints.GET_CURRENT_USERS.toString(),
                mapOf("chat_id" to chatId)
            )) {
                is ResponseState.Success -> {
                    Log.w("MessagesViewModel:getCurrentUsers", state.data.toString(2))
                    withContext(Dispatchers.Main) {
                        updateCurrentUsers(state.data)
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("MessagesViewModel:getCurrentUsers", state.errorText)
                }
            }
        }
    }

    fun searchUser(context: Context, source: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(context, userRepo).get(
                ServerEndpoints.SEARCH_USER.toString(),
                mapOf("match_str" to source)
            )) {
                is ResponseState.Success -> {
                    Log.w("MessagesViewModel:searchUser", state.data.toString(2))
                    withContext(Dispatchers.Main) {
                        updateFoundUsers(state.data)
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("MessagesViewModel:searchUser", "${state.code}: ${state.errorText}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context,
                            context.getString(R.string.users_not_found_text), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun addUserToChat(context: Context, chatId: String, userID: String) {
        val b = AddUserToChatAlertBinding.inflate(LayoutInflater.from(context))
        var role = "member"
        b.rgUserRole.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbCommon -> role = "member"
                R.id.rbObserver -> role = "observer"
            }
        }
        val alert = AlertDialog.Builder(context)
            .setView(b.root)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Добавить") { dialog, _ ->
                sendRequestToAddUser(context, chatId, userID, role)
                dialog.dismiss()
            }
        alert.create().show()
    }

    private fun sendRequestToAddUser(context: Context, chatId: String, userId: String, role: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(context, userRepo).put(
                ServerEndpoints.ADD_USER_TO_CHAT.toString(),
                mapOf("chat_id" to chatId, "user_id" to userId, "role" to role)
            )) {
                is ResponseState.Success -> {
                    val foundUser = foundUsers.find { it.userId == userId }
                    if (foundUser != null) currentUsers.add(foundUser.copy(is_selected = true))
                    withContext(Dispatchers.Main) {
                        currentUsersLiveData.value = currentUsers
                        foundUsers.remove(foundUser)
                        foundUsersLiveData.value = foundUsers
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("MessagesViewModel:sendRequestToAddUser", state.errorText)
                }
            }
        }
    }

    fun removeUserFromChat(context: Context, chatID: String, userID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(context, userRepo).put(
                ServerEndpoints.REMOVE_USER_FROM_CHAT.toString(),
                mapOf("chat_id" to chatID, "user_id" to userID)
            )) {
                is ResponseState.Success -> {
                    currentUsers.removeIf { it.userId == userID }
                    withContext(Dispatchers.Main) {
                        currentUsersLiveData.value = currentUsers
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("MessagesViewModel:removeUserFromChat", state.errorText)
                }
            }
        }
    }

    private fun updateFoundUsers(source: JSONObject) {
        if (foundUsers.isNotEmpty()) foundUsers.clear()
        val usersList = JasonStatham.string2ListJSONs(source["users"].toString())
        for (user in usersList) {
            val avatar = if (user["avatar_url"].toString() == "null") null
            else user["avatar_url"].toString()
            foundUsers.add(SelectedUserDataClass(
                publicId = user["public_id"].toString(),
                userId = user.getString("id"),
                avatar_photo_url = avatar,
                is_selected = false
            ))
        }
        foundUsersLiveData.value = foundUsers
    }

    private fun updateCurrentUsers(json: JSONObject) {
        if (currentUsers.isNotEmpty()) currentUsers.clear()
        val usersList = JasonStatham.string2ListJSONs(json["users"].toString())
        for (user in usersList) {
            currentUsers.add(
                SelectedUserDataClass(
                    userId = user.getString("id"),
                    name = user.getString("name"),
                    avatar_photo_url = user.optString("avatar_photo_url"),
                    chat_role = user.getString("chat_role"),
                    last_online = user.optString("last_online"),
                    can_exclude = user.getBoolean("can_exclude"),
                    is_selected = true
                )
            )
        }
        currentUsersLiveData.value = currentUsers
    }

    fun showMessageAction(context: Context, message: MessageDataClass) {
        val b = MessageActionsAlertBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(b.root)
    }

    override fun onCleared() {
        chatsRepo.clearMessagesLiveData()
        Log.w("MessagesViewModel:onCleared", "onCleared")
        super.onCleared()
    }
}