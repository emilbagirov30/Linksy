package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class DeleteRequestUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long): Response<Unit> {
        return channelRepository.deleteRequest(token, channelId)
    }
}