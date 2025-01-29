package com.emil.domain.usecase.user

import com.emil.domain.model.ChatResponse
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class GetUserChatsUseCase(private val chatRepository: ChatRepository) {
    suspend fun execute(token: String): Response<List<ChatResponse>> {
        return chatRepository.getUserChats(token)
    }
}
