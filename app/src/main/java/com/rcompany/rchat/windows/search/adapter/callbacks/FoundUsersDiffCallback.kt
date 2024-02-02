package com.rcompany.rchat.windows.search.adapter.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass

class FoundUsersDiffCallback(
    private val oldArray: ArrayList<FoundUsersDataClass>,
    private val newArray: ArrayList<FoundUsersDataClass>
) : DiffUtil.Callback(){
    override fun getOldListSize() = oldArray.size

    override fun getNewListSize() = newArray.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]
}