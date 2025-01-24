package com.emil.domain.usecase

import com.emil.domain.repository.MessageRepository

class ClearAllMessagesUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (){
        return messageRepository.clearAllMessages()
    }
}