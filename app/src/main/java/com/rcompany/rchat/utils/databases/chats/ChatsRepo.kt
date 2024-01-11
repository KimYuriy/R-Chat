package com.rcompany.rchat.utils.databases.chats

/**
 * Репозиторий для БД чатов и сообщений
 * @property chatsDB база данных чатов и сообщений типа [ChatsDB]
 */
class ChatsRepo private constructor(private val chatsDB: ChatsDB) {
    companion object {

        /**
         * Экземпляр репозитория типа null-[ChatsRepo]
         */
        @Volatile
        private var instance: ChatsRepo? = null

        /**
         * Функция получения экземпляра репозитория. Если он ранее был создан в ходе работы приложения, то
         * возвращается ранее созданный [instance], иначе создается новый, сохраняется и возвращается
         * @param chatsDB база данных чатов и сообщений типа [ChatsDB]
         * @return экземпляр репозитория [instance] типа [ChatsRepo]
         */
        fun getInstance(chatsDB: ChatsDB) =
            instance ?: synchronized(this) {
                instance ?: ChatsRepo(chatsDB).also {
                    instance = it
                }
            }
    }
}