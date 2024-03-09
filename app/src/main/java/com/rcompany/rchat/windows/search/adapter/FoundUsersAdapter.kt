package com.rcompany.rchat.windows.search.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.FoundUserItemBinding
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass
import com.rcompany.rchat.windows.messages.MessagesWindow
import com.rcompany.rchat.windows.search.adapter.callbacks.FoundUsersDiffCallback
import java.util.Base64

/**
 * Адаптер для отображения списка найденных пользователей
 */
class FoundUsersAdapter(
    private val array: ArrayList<FoundUsersDataClass>
) : RecyclerView.Adapter<FoundUsersAdapter.ViewHolder>() {
    inner class ViewHolder(private val b: FoundUserItemBinding) : RecyclerView.ViewHolder(b.root) {

        /**
         * Конструктор класса
         */
        init {
            itemView.setOnClickListener {
                val intent = Intent(
                    itemView.context,
                    MessagesWindow::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    putExtra("public_id", array[adapterPosition].publicId)
                }
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                b.cbSelected.visibility = View.VISIBLE
                b.cbSelected.isChecked = true
                true
            }

            b.cbSelected.setOnCheckedChangeListener { _, checked ->
                b.cbSelected.isChecked = checked
                if (checked) {
                    //TODO: Добавить добавление ID пользователя в массив для создания беседы
                } else {
                    //TODO: Добавить удаление ID пользователя из массива для создания беседы
                }
            }
        }

        /**
         * Установка параметров найденного пользователя
         */
        fun bind(data: FoundUsersDataClass) {
            b.tvLogin.text = data.publicId

            if (data.avatarUrl != null) {
                val bytes = Base64.getDecoder().decode(data.avatarUrl)
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

    /**
     * Функция обновления списка найденных пользователей
     */
    fun updateFoundUsers(newArray: ArrayList<FoundUsersDataClass>) {
        val dr = DiffUtil.calculateDiff(FoundUsersDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}