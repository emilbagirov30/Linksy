package com.emil.domain.usecase

import com.emil.domain.model.MessageLocal
import com.emil.domain.repository.MessageRepository

class GetUserMessagesByChatFromLocalDb(private val messageRepository: MessageRepository) {
    suspend fun execute (chatId:Long):MutableList<MessageLocal>{
       return messageRepository.getMessagesByChatFromLocalDb(chatId);

    }
}