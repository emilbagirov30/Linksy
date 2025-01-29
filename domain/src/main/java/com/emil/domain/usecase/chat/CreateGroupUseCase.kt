package com.emil.domain.usecase.chat;

import com.emil.domain.model.GroupData
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class CreateGroupUseCase (private val chatRepository: ChatRepository){
    suspend fun execute (token:String,groupData: GroupData):Response<Unit>{
        return chatRepository.createGroup(token,groupData)
    }
}