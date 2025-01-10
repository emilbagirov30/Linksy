package com.emil.domain.usecase

import com.emil.domain.model.MessageLocal
import com.emil.domain.repository.MessageRepository

class GetUserMessagesFromLocalDb(private val messageRepository: MessageRepository) {
    suspend fun execute (chatId:Long):List<MessageLocal>{
       return messageRepository.getMessagesByChat(chatId);

    }
}