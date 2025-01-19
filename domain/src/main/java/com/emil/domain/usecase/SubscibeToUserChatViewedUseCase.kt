package com.emil.domain.usecase

import com.emil.domain.model.ChatResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToUserChatViewedUseCase (private val repository: WebSocketRepository) {
    operator fun invoke(token: String,chatId:Long): Flow<Long> {
        return repository.subscribeToUserChatViewed(token,chatId)
    }}