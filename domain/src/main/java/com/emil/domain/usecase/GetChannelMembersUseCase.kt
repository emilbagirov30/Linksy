package com.emil.domain.usecase

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class GetChannelMembersUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long): Response<List<UserResponse>> {
        return channelRepository.getChannelMembers(token, channelId)
    }
}