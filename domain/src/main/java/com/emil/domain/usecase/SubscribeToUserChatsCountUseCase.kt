package com.emil.domain.usecase

import com.emil.domain.model.ChatResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToUserChatsCountUseCase (private val repository: WebSocketRepository) {
    operator fun invoke(token: String): Flow<ChatResponse> {
        return repository.subscribeToUserChatsCount(token)
    }
}