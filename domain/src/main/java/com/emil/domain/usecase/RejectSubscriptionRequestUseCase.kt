package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class RejectSubscriptionRequestUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long,candidateId:Long): Response<Unit> {
        return channelRepository.rejectSubscriptionRequest(token, channelId,candidateId)
    }
}