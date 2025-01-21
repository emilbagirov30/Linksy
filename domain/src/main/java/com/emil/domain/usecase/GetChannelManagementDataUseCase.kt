package com.emil.domain.usecase

import com.emil.domain.model.ChannelManagementResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response


class GetChannelManagementDataUseCase(private val channelRepository: ChannelRepository) {
    suspend fun execute (token:String,channelId:Long): Response<ChannelManagementResponse>{
        return  channelRepository.getChannelManagementData(token, channelId)
    }
}