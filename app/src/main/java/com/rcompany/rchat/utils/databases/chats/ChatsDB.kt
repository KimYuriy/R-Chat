package com.rcompany.rchat.utils.databases.chats

/**
 * База данных мессенджера - чаты и сообщения
 */
class ChatsDB private constructor() {
    companion object {
        /**
         * Экземпляр БД типа null-[ChatsDB]
         */
        @Volatile
        private var instance: ChatsDB? = null

        /**
         * Функция получения экземпляра БД. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @return экземпляр БД [instance] типа [ChatsDB]
         */
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ChatsDB().also {
                    instance = it
            }
        }
    }
}