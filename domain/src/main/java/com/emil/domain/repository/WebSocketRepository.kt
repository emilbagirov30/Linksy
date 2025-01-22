package com.emil.domain.repository

import com.emil.domain.model.ChatResponse
import com.emil.domain.model.MessageResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun connect()
    fun disconnect()
    fun subscribeToUserMessages(token: String,chatId:Long): Flow<MessageResponse>
    fun subscribeToUserChats(token: String): Flow<ChatResponse>
    fun subscribeToUserChatsCount(token: String): Flow<ChatResponse>
    fun subscribeToUserChatViewed(token: String,chatId: Long): Flow<Long>
    fun subscribeToDeletedMessages(token: String,chatId: Long): Flow<Long>
}