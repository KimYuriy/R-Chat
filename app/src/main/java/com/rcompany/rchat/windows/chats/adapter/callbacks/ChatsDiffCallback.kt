package com.rcompany.rchat.windows.chats.adapter.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.utils.databases.chats.dataclasses.chats.ChatDataClass

/**
 * Класс для сравнения двух массивов чатов
 * @property oldArray старый массив чатов типа [ArrayList]
 * @property newArray новый массив чатов типа [ArrayList]
 */
class ChatsDiffCallback(
    private val oldArray: ArrayList<ChatDataClass>,
    private val newArray: ArrayList<ChatDataClass>
): DiffUtil.Callback() {

    /**
     * Функция получения размера старого массива
     */
    override fun getOldListSize() = oldArray.size

    /**
     * Функция получения размера нового массива
     */
    override fun getNewListSize() = newArray.size

    /**
     * Функция для сравнения двух элементов массивов чатов
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]

    /**
     * Функция для сравнения содержания двух элементов массивов чатов
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]
}