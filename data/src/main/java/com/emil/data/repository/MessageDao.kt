package com.emil.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emil.data.model.MessageLocalDb
import com.emil.domain.model.MessageData

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    suspend fun getMessagesByChat(chatId: Long): MutableList<MessageLocalDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageLocalDb)
}