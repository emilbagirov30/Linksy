package com.emil.domain.repository

import com.emil.domain.model.ChatResponse
import com.emil.domain.model.EditMessageResponse
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.Status
import com.emil.domain.model.StatusResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun connect()
    fun disconnect()
    fun subscribeToUserMessages(token: String,chatId:Long): Flow<MessageResponse>
    fun subscribeToUserChats(token: String): Flow<ChatResponse>
    fun subscribeToUserChatsCount(token: String): Flow<ChatResponse>
    fun subscribeToUserChatViewed(token: String,chatId: Long): Flow<Long>
    fun subscribeToDeletedMessages(token: String,chatId: Long): Flow<Long>
    fun subscribeToEditMessages(token: String,chatId: Long): Flow<EditMessageResponse>
    fun sendStatus(status: Status)
    fun subscribeToStatusMessages(token: String,chatId: Long): Flow<StatusResponse>
}