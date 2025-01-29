package com.emil.domain.usecase.websocket

import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToMessagesDeleteUseCase (private val repository: WebSocketRepository) {
    fun invoke(token: String,chatId:Long): Flow<Long> {
        return repository.subscribeToDeletedMessages(token, chatId)
    }
}