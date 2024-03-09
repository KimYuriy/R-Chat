package com.rcompany.rchat.windows.messages.adapter.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.utils.databases.chats.ReceivedMessageDataClass

/**
 * Класс для сравнения двух массивов сообщений
 * @property oldArray старый массив сообщений типа [ArrayList]
 * @property newArray новый массив сообщений типа [ArrayList]
 */
class MessagesDiffCallback(
    private val oldArray: ArrayList<ReceivedMessageDataClass>,
    private val newArray: ArrayList<ReceivedMessageDataClass>
): DiffUtil.Callback() {

    /**
     * Функция получения размера старого массива сообщений
     */
    override fun getOldListSize() = oldArray.size

    /**
     * Функция получения размера нового массива сообщений
     */
    override fun getNewListSize() = newArray.size

    /**
     * Функция для сравнения двух элементов массивов сообщений
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]

    /**
     * Функция для сравнения содержимого двух элементов массивов сообщений
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]
}