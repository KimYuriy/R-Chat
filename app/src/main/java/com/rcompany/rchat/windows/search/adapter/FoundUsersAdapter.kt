package com.rcompany.rchat.windows.search.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.FoundUserItemBinding
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass
import com.rcompany.rchat.windows.search.adapter.callbacks.FoundUsersDiffCallback
import java.util.Base64

class FoundUsersAdapter(
    private val array: ArrayList<FoundUsersDataClass>
) : RecyclerView.Adapter<FoundUsersAdapter.ViewHolder>() {
    inner class ViewHolder(private val b: FoundUserItemBinding) : RecyclerView.ViewHolder(b.root) {

        init {
            itemView.setOnLongClickListener {
                b.cbSelected.visibility = View.VISIBLE
                b.cbSelected.isChecked = true
                true
            }

            b.cbSelected.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    //TODO: Добавить добавление ID пользователя в массив для создания беседы
                } else {
                    //TODO: Добавить удаление ID пользователя из массива для создания беседы
                }
            }
        }

        fun bind(data: FoundUsersDataClass) {
            b.tvLogin.text = data.login

            if (data.avatarString != null) {
                val bytes = Base64.getDecoder().decode(data.avatarString)
                b.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
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

    fun updateFoundUsers(newArray: ArrayList<FoundUsersDataClass>) {
        val dr = DiffUtil.calculateDiff(FoundUsersDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}