package com.emil.domain.usecase

import com.emil.domain.model.GroupResponse
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class GetGroupDataUseCase (private val chatRepository: ChatRepository){
    suspend fun execute (token:String,groupId:Long): Response<GroupResponse>{
        return chatRepository.getGroupData(token, groupId)
    }
}