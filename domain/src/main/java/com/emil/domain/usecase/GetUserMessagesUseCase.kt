package com.emil.domain.usecase

import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class GetUserMessagesUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (token:String):Response<List<MessageResponse>>{
        return messageRepository.getUserMessages(token)
    }
}