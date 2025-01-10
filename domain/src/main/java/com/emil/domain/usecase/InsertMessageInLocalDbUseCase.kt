package com.emil.domain.usecase

import com.emil.domain.model.MessageLocal
import com.emil.domain.repository.MessageRepository

class InsertMessageInLocalDbUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute(messageLocal: MessageLocal){
        messageRepository.insertMessage(messageLocal)
    }
}