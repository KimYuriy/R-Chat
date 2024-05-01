package com.rcompany.rchat.windows.create_group_chat.adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.windows.create_group_chat.dataclasses.SelectedUserDataClass

class SelectedUsersDiffCallback(
    private val oldArray: ArrayList<SelectedUserDataClass>,
    private val newArray: ArrayList<SelectedUserDataClass>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldArray.size

    override fun getNewListSize() = newArray.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldArray[oldItemPosition] == newArray[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldArray[oldItemPosition] == newArray[newItemPosition]
}