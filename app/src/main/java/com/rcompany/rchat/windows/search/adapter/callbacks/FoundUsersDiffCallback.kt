package com.rcompany.rchat.windows.search.adapter.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.rcompany.rchat.utils.databases.window_dataclasses.FoundUsersDataClass

/**
 * Класс для сравнения двух массивов найденных пользователей
 * @property oldArray старый массив найденных пользователей типа [ArrayList]
 * @property newArray новый массив найденных пользователей типа [ArrayList]
 */
class FoundUsersDiffCallback(
    private val oldArray: ArrayList<FoundUsersDataClass>,
    private val newArray: ArrayList<FoundUsersDataClass>
) : DiffUtil.Callback(){

    /**
     * Функция получения размера старого массива
     */
    override fun getOldListSize() = oldArray.size

    /**
     * Функция получения размера нового массива
     */
    override fun getNewListSize() = newArray.size

    /**
     * Функция для сравнения элементов двух массивов
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]

    /**
     * Функция для сравнения содержимого элементов двух массивов
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldArray[oldItemPosition] == newArray[newItemPosition]
}