package com.rcompany.rchat.windows.chats.adapter.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.utils.databases.chats.ChatDataClass

class ChatsDiffCallback(private val oldArray: ArrayList<ChatDataClass>, private val newArray: ArrayList<ChatDataClass>): DiffUtil.Callback() {
    override fun getOldListSize() = oldArray.size

    override fun getNewListSize() = newArray.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]
}