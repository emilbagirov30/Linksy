package com.emil.domain.usecase.message;

import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class GetUserMessagesByChat (private val messageRepository: MessageRepository) {
    suspend fun execute (token:String,chatId:Long):Response<MutableList<MessageResponse>>{
        return messageRepository.getMessagesByChat(token,chatId)
    }
}