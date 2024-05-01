package com.rcompany.rchat.windows.messages.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.MessageItemIncomingBinding
import com.rcompany.rchat.databinding.MessageItemOutgoingBinding
import com.rcompany.rchat.databinding.SystemMessageItemBinding
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.incoming.MessageDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.utils.databases.chats.enums.MessageTypes
import com.rcompany.rchat.utils.extensions.formatTime
import com.rcompany.rchat.windows.messages.adapter.callbacks.MessagesDiffCallback

/**
 * Адаптер сообщений
 * @property array массив сообщений типа [ArrayList]
 * @property userId id текущего пользователя
 */
class MessageItemAdapter(
    private val array: ArrayList<MessageDataClass>,
    private val userId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_INCOMING = 0
        private const val TYPE_OUTGOING = 1
        private const val TYPE_SYSTEM = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (array[position].sender.user_id == userId) {
            TYPE_OUTGOING
        } else {
            when (array[position].type) {
                MessageTypes.CREATED_CHAT.value, MessageTypes.USER_REMOVED.value -> TYPE_SYSTEM
                else -> TYPE_INCOMING
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_INCOMING -> {
                val b = MessageItemIncomingBinding.inflate(layoutInflater, parent, false)
                IncomingViewHolder(b)
            }
            TYPE_OUTGOING -> {
                val b = MessageItemOutgoingBinding.inflate(layoutInflater, parent, false)
                OutgoingViewHolder(b)
            }
            else -> {
                val binding = SystemMessageItemBinding.inflate(layoutInflater, parent, false)
                SystemInfoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = array[position]
        when (getItemViewType(position)) {
            TYPE_INCOMING -> {
                (holder as IncomingViewHolder).bind(item)
            }
            TYPE_OUTGOING -> {
                (holder as OutgoingViewHolder).bind(item)
            }
            else -> {
                (holder as SystemInfoViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount() = array.size

    inner class IncomingViewHolder(private val b: MessageItemIncomingBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            b.root.setOnClickListener {
                Log.d("MessageItemAdapter", "Clicked: ${array[adapterPosition].sender.user_id}")
            }
        }

        fun bind(data: MessageDataClass) {
            b.tvMessage.text = data.message_text
            b.tvTime.text = formatTime(data.created_at)

            if (data.chat.type == ChatTypes.PRIVATE.value) {
                b.tvLogin.visibility = View.GONE
                b.cvAvatar.visibility = View.GONE
            } else {
                b.tvLogin.text = data.sender.name
            }
        }
    }

    inner class OutgoingViewHolder(private val b: MessageItemOutgoingBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            b.root.setOnClickListener {
                Log.d("MessageItemAdapter", "Clicked: ${array[adapterPosition].message_text}")
            }
        }

        fun bind(data: MessageDataClass) {
            b.tvMessage.text = data.message_text
            b.tvTime.text = formatTime(data.created_at)
        }
    }

    inner class SystemInfoViewHolder(private val b: SystemMessageItemBinding): RecyclerView.ViewHolder(b.root) {
        fun bind(data: MessageDataClass) {
            b.tvSystem.text = "Created chat ${data.sender.name}"
        }
    }

    fun updateMessages(newArray: ArrayList<MessageDataClass>) {
        val dr = DiffUtil.calculateDiff(MessagesDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}
