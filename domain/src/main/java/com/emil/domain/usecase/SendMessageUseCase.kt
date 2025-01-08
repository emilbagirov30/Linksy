package com.emil.domain.usecase

import com.emil.domain.model.MessageData
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class SendMessageUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (token:String,message:MessageData):Response<Unit>{
        return messageRepository.sendMessage(token,message)
    }
}