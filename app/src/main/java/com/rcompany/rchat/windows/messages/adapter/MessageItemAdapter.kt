package com.rcompany.rchat.windows.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcompany.rchat.databinding.MessageItemBinding
import com.rcompany.rchat.utils.databases.chats.ReceivedMessageDataClass
import com.rcompany.rchat.windows.messages.adapter.callbacks.MessagesDiffCallback

/**
 * Адаптер сообщений
 * @property array массив сообщений типа [ArrayList]
 * @property userId id текущего пользователя
 */
class MessageItemAdapter(
    private val array: ArrayList<ReceivedMessageDataClass>,
    private val userId: String
) : RecyclerView.Adapter<MessageItemAdapter.ViewHolder>() {

     inner class ViewHolder(private val b: MessageItemBinding): RecyclerView.ViewHolder(b.root) {
        init {

        }

         /**
          * Установка элементов в отображаемом сообщении
          */
         fun  bind(data: ReceivedMessageDataClass) {

         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(array[position])
    }

    override fun getItemCount() = array.size

    /**
     * Обновление списка сообщений
     */
    fun updateMessages(newArray: ArrayList<ReceivedMessageDataClass>) {
        val dr = DiffUtil.calculateDiff(MessagesDiffCallback(array, newArray))
        array.clear()
        array.addAll(newArray)
        dr.dispatchUpdatesTo(this)
    }
}