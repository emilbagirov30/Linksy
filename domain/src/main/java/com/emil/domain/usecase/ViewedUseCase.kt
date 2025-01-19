package com.emil.domain.usecase

import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class ViewedUseCase (private val messageRepository: MessageRepository) {
    suspend fun execute (token:String, chatId:Long):Response<Unit>{
        return messageRepository.viewed(token, chatId)
    }
}