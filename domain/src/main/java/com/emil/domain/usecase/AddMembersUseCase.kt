package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class AddMembersUseCase (private val chatRepository: ChatRepository){
    suspend fun execute (token:String,groupId:Long,newMembers:List<Long>):Response<Unit>{
        return chatRepository.addMembers(token,groupId, newMembers)
    }
}