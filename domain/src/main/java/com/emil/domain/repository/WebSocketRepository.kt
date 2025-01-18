package com.emil.domain.repository

import com.emil.domain.model.MessageResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun connect()
    fun disconnect()
    fun subscribeToUserMessages(token: String,chatId:Long): Flow<MessageResponse>
}