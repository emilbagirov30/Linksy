package com.emil.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emil.data.model.ChatLocalDb

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats")
    suspend fun getAllChats(): List<ChatLocalDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatLocalDb)

    @Query("SELECT isGroup FROM chats WHERE id = :chatId LIMIT 1")
    suspend fun isGroup(chatId: Long): Boolean
}