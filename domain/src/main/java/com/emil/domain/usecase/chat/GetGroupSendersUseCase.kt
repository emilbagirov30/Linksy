package com.emil.domain.usecase.chat

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class GetGroupSendersUseCase (private val chatRepository: ChatRepository) {
    suspend fun execute (token:String,groupId:Long): Response<List<UserResponse>> {
        return chatRepository.getGroupSenders(token,groupId)
    }
}
