package com.rcompany.rchat.utils

import org.json.JSONObject

/**
 * Класс для работы с json
 */
class JasonStatham private constructor() {
    companion object {

        /**
         * Функция преобразования строки в массив json
         * @param source строка типа [String]
         * @return массив json типа [List]
         */
        fun string2ListJSONs(source: String): List<JSONObject> {
            val jsonList = mutableListOf<JSONObject>()
            val json = source.drop(1).dropLast(1)
            var counter = 0
            var startInd = 0
            json.forEachIndexed { index, char ->
                if (char == '{') counter++
                if (char == '}') counter--
                if (index == json.length - 1) jsonList.add(JSONObject(json.substring(startInd)))
                else if ((char == ',') && (counter == 0)) {
                    jsonList.add(JSONObject(json.substring(startInd, index)))
                    startInd = index + 1
                }
            }
            return jsonList
        }
    }
}