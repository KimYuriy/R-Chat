package com.rcompany.rchat.windows.create_group_chat.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rcompany.rchat.databinding.NewGroupChatDataBinding
import com.rcompany.rchat.utils.JasonStatham
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.utils.network.NetworkManager
import com.rcompany.rchat.utils.network.address.ServerEndpoints
import com.rcompany.rchat.utils.network.requests.ResponseState
import com.rcompany.rchat.windows.create_group_chat.dataclasses.SelectedUserDataClass
import com.rcompany.rchat.windows.messages.MessagesWindow
import com.rcompany.rchat.windows.messages.dataclasses.DataForMessagesWindowDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CreateGroupChatViewModel(private val userRepo: UserRepo): ViewModel() {
    private var selectedUsers = ArrayList<SelectedUserDataClass>()
    private var foundUsers = ArrayList<SelectedUserDataClass>()
    val selectedUsersLiveData = MutableLiveData<ArrayList<SelectedUserDataClass>>()
    val foundUsersLiveData = MutableLiveData<ArrayList<SelectedUserDataClass>>()

    init {
        selectedUsers = ArrayList()
        foundUsers = ArrayList()
        selectedUsersLiveData.value = selectedUsers
        foundUsersLiveData.value = foundUsers
    }

    fun addSelectedUser(userId: String) {
        val selectedUser = foundUsers.find { it.userId == userId }
        if (selectedUser != null) selectedUsers.add(selectedUser.copy(is_selected = true))
        Log.d("selectedUsers", selectedUsers.toString())
        selectedUsersLiveData.value = selectedUsers
        foundUsers.remove(selectedUser)
        foundUsersLiveData.value = foundUsers
    }

    fun removeSelectedUser(userId: String) {
        val selectedUser = selectedUsers.find { it.userId == userId }
        selectedUsers.remove(selectedUser)
        selectedUsersLiveData.value = selectedUsers
    }

    fun showAlertDialog(context: Context) {
        val binding = NewGroupChatDataBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton("Создать") { _, _ ->
                val chatName = binding.etChatName.text.toString()
                var startTime: String? = null
                var endTime: String? = null
                if (binding.cbWorkChat.isChecked) {
                    val startTimeHours = if (binding.tpStartTime.hour.toString().length == 1) "0" + binding.tpStartTime.hour.toString() else binding.tpStartTime.hour.toString()
                    val startTimeMinutes = if (binding.tpStartTime.minute.toString().length == 1) "0" + binding.tpStartTime.minute.toString() else binding.tpStartTime.minute.toString()
                    startTime = "$startTimeHours:$startTimeMinutes"
                    val endTimeHours = if (binding.tpEndTime.hour.toString().length == 1) "0" + binding.tpEndTime.hour.toString() else binding.tpEndTime.hour.toString()
                    val endTimeMinutes = if (binding.tpEndTime.minute.toString().length == 1) "0" + binding.tpEndTime.minute.toString() else binding.tpEndTime.minute.toString()
                    endTime = "$endTimeHours:$endTimeMinutes"
                }
                val usersIDs = selectedUsersLiveData.value?.map { it.userId }
                CoroutineScope(Dispatchers.IO).launch {
                    when (val state = NetworkManager(context.applicationContext, userRepo).post(
                        ServerEndpoints.CHAT_CREATE.toString(),
                        mapOf(
                            "user_id_list" to usersIDs,
                            "name" to chatName,
                            "description" to null,
                            "is_work_chat" to binding.cbWorkChat.isChecked,
                            "allow_messages_from" to startTime,
                            "allow_messages_to" to endTime,
                        ),
                        true
                    )) {
                        is ResponseState.Success -> {
                            Log.w("CreateGroupChatViewModel:showAlertDialog", state.data.toString(2))
                            val groupChatData = state.data.getJSONObject("created_chat_info")
                            val is_work_chat = groupChatData.getBoolean("is_work_chat")
                            withContext(Dispatchers.Main) {
                                val data = DataForMessagesWindowDataClass(
                                    chat_id = groupChatData.getString("id"),
                                    chat_name = groupChatData.getString("name"),
                                    chat_type = if (is_work_chat) ChatTypes.WORK.value else ChatTypes.GROUP.value,
                                    is_work_chat = is_work_chat,
                                    allow_messages_from = groupChatData.getString("allow_messages_from"),
                                    allow_messages_to = groupChatData.getString("allow_messages_to"),
                                    chat_avatar = groupChatData.optString("avatar_photo_url"),
                                )
                                val intent = Intent(context, MessagesWindow::class.java)
                                    .putExtra("chat_data", data)
                                context.startActivity(intent)
                            }
                        }
                        is ResponseState.Failure -> {

                        }
                    }
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
        binding.cbWorkChat.setOnCheckedChangeListener { _, isChecked ->
            binding.llWorkTime.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        binding.tpEndTime.setIs24HourView(true)
        binding.tpStartTime.setIs24HourView(true)
        dialog.show()
    }

    fun searchUser(context: Context, payload: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val state = NetworkManager(context.applicationContext, userRepo).get(
                ServerEndpoints.SEARCH_USER.toString(),
                mapOf("match_str" to payload)
            )) {
                is ResponseState.Success -> {
                    withContext(Dispatchers.Main) {
                        updateFoundUsers(state.data)
                    }
                }
                is ResponseState.Failure -> {
                    Log.e("CreateGroupChatViewModel:searchUser", "${state.code}: ${state.errorText}")

                }
            }
        }
    }

    private fun updateFoundUsers(source: JSONObject) {
        if (foundUsers.isNotEmpty()) foundUsers.clear()
        Log.d("SearchViewModel:updateFoundUsers", "$source")
        val usersList = JasonStatham.string2ListJSONs(source["users"].toString())
        for (user in usersList) {
            val avatar = if (user["avatar_url"].toString() == "null") null
            else user["avatar_url"].toString()
            foundUsers.add(
                SelectedUserDataClass(
                    publicId = user["public_id"].toString(),
                    userId = user.getString("id"),
                    avatar_photo_url = avatar,
                    is_selected = false
                )
            )
        }
        foundUsersLiveData.value = foundUsers
    }
}