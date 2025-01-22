package com.emil.domain.usecase

import com.emil.domain.model.EditMessageResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToEditMessagesUseCase (private val repository: WebSocketRepository) {
    operator fun invoke(token: String,chatId:Long): Flow<EditMessageResponse> {
        return repository.subscribeToEditMessages(token, chatId)
    }
}