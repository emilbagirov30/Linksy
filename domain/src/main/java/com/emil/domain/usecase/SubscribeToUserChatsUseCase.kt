package com.emil.domain.usecase

import com.emil.domain.model.ChatResponse
import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToUserChatsUseCase (private val repository: WebSocketRepository) {
     fun invoke(token: String): Flow<ChatResponse> {
        return repository.subscribeToUserChats(token)
    }
}