package com.emil.domain.usecase.channel


import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class AcceptUserToChannelUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long,candidateId:Long): Response<Unit> {
        return channelRepository.acceptUserToChannel(token, channelId,candidateId)
    }
}