package com.emil.domain.usecase.channel

import com.emil.domain.model.ChannelData
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class CreateChannelUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (token:String,channelData: ChannelData):Response<Unit>{
        return channelRepository.createChannel(token,channelData)
    }
}