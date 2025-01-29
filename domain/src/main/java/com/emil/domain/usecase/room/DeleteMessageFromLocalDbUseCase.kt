package com.emil.domain.usecase.room

import com.emil.domain.repository.MessageRepository

class DeleteMessageFromLocalDbUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (messageId:Long){
        return messageRepository.deleteMessageFromLocalDb(messageId)
    }
}