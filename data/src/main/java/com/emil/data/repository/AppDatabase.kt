package com.emil.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emil.data.model.ChatLocalDb
import com.emil.data.model.MessageLocalDb

@Database(entities = [MessageLocalDb::class, ChatLocalDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
}
