package com.rcompany.rchat.windows.chats.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.ChatItemBinding
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.incoming.ChatDataClass
import com.rcompany.rchat.utils.databases.chats.enums.ChatTypes
import com.rcompany.rchat.utils.extensions.formatTime
import com.rcompany.rchat.windows.chats.adapter.callbacks.ChatsDiffCallback
import com.rcompany.rchat.windows.chats.interfaces.ChatItemInterface
import com.rcompany.rchat.windows.messages.MessagesWindow
import com.rcompany.rchat.windows.messages.dataclasses.DataForMessagesWindowDataClass

/**
 * Адаптер для списка чатов
 */
class ChatItemAdapter(
    private val array: ArrayList<ChatDataClass>,
    private val userId: String,
    private val i: ChatItemInterface
) : RecyclerView.Adapter<ChatItemAdapter.ViewHolder>() {
    inner class ViewHolder(private val b: ChatItemBinding): RecyclerView.ViewHolder(b.root) {

        init {
            itemView.setOnClickListener {
                val data = DataForMessagesWindowDataClass(
                    chat_id = array[adapterPosition].id,
                    chat_name = array[adapterPosition].name,
                    chat_type = array[adapterPosition].type,
                    chat_avatar = array[adapterPosition].avatar_photo_url,
                    is_work_chat = false,
                    allow_messages_from = array[adapterPosition].allow_messages_from,
                    allow_messages_to = array[adapterPosition].allow_messages_to,
                )
                val intent = Intent(itemView.context, MessagesWindow::class.java).putExtra("chat_data", data)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                i.onLongClicked(array[adapterPosition])
                true
            }
        }

        /**
         * Установка данных чата
         */
        fun bind(data: ChatDataClass) {
            b.tvLogin.text = data.name
            b.tvMessage.text = data.last_message?.message_text
            val time = formatTime(data.last_message?.created_at)
            if (time != null) {
                b.tvDatetime.text = time
            }

//            if (data.avatar_photo_url != null) {
//                val bytes = Base64.getDecoder().decode(data.avatar_photo_url)
//                b.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
//            }

            b.tvYou.visibility = if (data.type == ChatTypes.GROUP.value) {
                if (data.last_message?.sender?.user_id != userId) {
                    b.tvYou.text = data.last_message?.sender?.name
                }
                View.VISIBLE
            } else {
                if (data.last_message?.sender?.user_id == userId) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(array[position])
    }

    override fun getItemCount() = array.size

    /**
     * Обновление списка чатов
     */
    fun updateChats(newArray: ArrayList<ChatDataClass>) {
        val dr = DiffUtil.calculateDiff(ChatsDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}