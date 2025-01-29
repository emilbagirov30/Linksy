package com.emil.domain.usecase.chat

import com.emil.domain.repository.ChatRepository
import retrofit2.Response


class GetChatIdUseCase (private val chatRepository: ChatRepository) {
    suspend fun execute (token:String,userId:Long): Response<Long> {
        return chatRepository.getChatId(token,userId)
    }
}