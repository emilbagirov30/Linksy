package com.emil.domain.usecase.room

import com.emil.domain.repository.MessageRepository

class ClearAllMessagesUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (){
        return messageRepository.clearAllMessages()
    }
}