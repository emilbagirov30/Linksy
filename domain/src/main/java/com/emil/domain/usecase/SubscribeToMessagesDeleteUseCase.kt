package com.emil.domain.usecase

import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToMessagesDeleteUseCase (private val repository: WebSocketRepository) {
    operator fun invoke(token: String,chatId:Long): Flow<Long> {
        return repository.subscribeToDeletedMessages(token, chatId)
    }
}