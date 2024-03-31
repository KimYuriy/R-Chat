package com.rcompany.rchat.windows.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.MessageItemIncomingBinding
import com.rcompany.rchat.databinding.MessageItemOutgoingBinding
import com.rcompany.rchat.utils.databases.chats.dataclasses.messages.MessageDataClass
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
    }

    override fun getItemViewType(position: Int): Int {
        return if (array[position].sender_user_id == userId) TYPE_OUTGOING else TYPE_INCOMING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_INCOMING) {
            val b = MessageItemIncomingBinding.inflate(layoutInflater, parent, false)
            IncomingViewHolder(b)
        } else {
            val b = MessageItemOutgoingBinding.inflate(layoutInflater, parent, false)
            OutgoingViewHolder(b)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = array[position]
        if (getItemViewType(position) == TYPE_INCOMING) {
            (holder as IncomingViewHolder).bind(item)
        } else {
            (holder as OutgoingViewHolder).bind(item)
        }
    }

    override fun getItemCount() = array.size

    inner class IncomingViewHolder(private val b: MessageItemIncomingBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(data: MessageDataClass) {
            b.tvMessage.text = data.message_text
            b.tvTime.text = data.created_at
        }
    }

    inner class OutgoingViewHolder(private val b: MessageItemOutgoingBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(data: MessageDataClass) {
            b.tvMessage.text = data.message_text
            b.tvTime.text = data.created_at
        }
    }

    fun updateMessages(newArray: ArrayList<MessageDataClass>) {
        val dr = DiffUtil.calculateDiff(MessagesDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}
