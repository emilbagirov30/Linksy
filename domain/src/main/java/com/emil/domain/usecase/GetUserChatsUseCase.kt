package com.emil.domain.usecase

import com.emil.domain.model.ChatResponse
import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.ChatRepository
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class GetUserChatsUseCase(private val chatRepository: ChatRepository) {
    suspend fun execute(token: String): Response<List<ChatResponse>> {
        return chatRepository.getUserChats(token)
    }
}
