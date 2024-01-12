package com.rcompany.rchat.windows.chats.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.ChatItemBinding
import com.rcompany.rchat.utils.databases.chats.ChatDataClass
import com.rcompany.rchat.windows.chats.adapter.callbacks.ChatsDiffCallback
import java.util.Base64

class ChatItemAdapter(private val array: ArrayList<ChatDataClass>, private val userId: Int): RecyclerView.Adapter<ChatItemAdapter.ViewHolder>() {
    inner class ViewHolder(private val b: ChatItemBinding): RecyclerView.ViewHolder(b.root) {
        fun bind(data: ChatDataClass) {
            b.tvLogin.text = data.login
            b.tvMessage.text = data.message
            b.tvDatetime.text = data.time

            if (data.avatar != null) {
                val bytes = Base64.getDecoder().decode(data.avatar)
                b.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }

            b.tvYou.visibility = if (data.lastMessageSenderId == userId) {
                View.VISIBLE
            } else {
                View.GONE
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

    fun updateChats(newArray: ArrayList<ChatDataClass>) {
        val dr = DiffUtil.calculateDiff(ChatsDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}