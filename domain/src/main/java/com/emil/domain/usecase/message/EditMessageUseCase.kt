package com.emil.domain.usecase.message

import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class EditMessageUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (token:String,messageId:Long,text:String):Response<Unit>{
        return messageRepository.editMessage(token, messageId, text)
    }
}