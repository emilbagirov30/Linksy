package com.emil.domain.usecase

import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class DeleteMessageUseCase (private val messageRepository: MessageRepository){
    suspend fun execute (token:String,messageId:Long):Response<Unit>{
        return messageRepository.deleteMessage(token, messageId)
    }
}