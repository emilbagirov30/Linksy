package com.emil.domain.usecase.channel;

import com.emil.domain.model.ChannelResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class GetChannelsUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String): Response<List<ChannelResponse>> {
        return channelRepository.getUserChannels(token = token)
    }
}


