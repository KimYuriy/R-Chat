package com.rcompany.rchat.windows.create_group_chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.FoundUserItemBinding
import com.rcompany.rchat.windows.create_group_chat.adapters.callbacks.SelectedUsersDiffCallback
import com.rcompany.rchat.windows.create_group_chat.dataclasses.SelectedUserDataClass
import com.rcompany.rchat.windows.create_group_chat.interfaces.CreateGroupChatInterface

class SelectedUserAdapter(
    private val array: ArrayList<SelectedUserDataClass>,
    private val i: CreateGroupChatInterface
): RecyclerView.Adapter<SelectedUserAdapter.ViewHolder>() {
    inner class ViewHolder(private val b: FoundUserItemBinding): RecyclerView.ViewHolder(b.root) {

        fun bind(item: SelectedUserDataClass) {
            b.tvLogin.text = item.publicId ?: item.name
            b.cbSelected.visibility = View.VISIBLE
            if (array[adapterPosition].is_selected != null)
                b.cbSelected.isChecked = array[adapterPosition].is_selected!!

            b.cbSelected.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    i.onUserSelected(array[adapterPosition].userId!!)
                } else {
                    i.onUserUnselected(array[adapterPosition].userId!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = FoundUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(array[position])
    }

    override fun getItemCount() = array.size

    fun updateArray(newArray: ArrayList<SelectedUserDataClass>) {
        val df = DiffUtil.calculateDiff(SelectedUsersDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        df.dispatchUpdatesTo(this)
    }
}