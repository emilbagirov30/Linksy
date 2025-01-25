package com.emil.domain.usecase

import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class LeaveTheGroupUseCase (private val chatRepository: ChatRepository){
    suspend fun execute (token:String,groupId:Long): Response<Unit> {
        return chatRepository.leaveTheGroup(token,groupId)
    }
}