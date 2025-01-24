package com.emil.domain.usecase

import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToUserMessagesUseCase(private val repository: WebSocketRepository) {
    fun invoke(token: String,chatId:Long): Flow<MessageResponse> {
        return repository.subscribeToUserMessages(token,chatId)
    }
}