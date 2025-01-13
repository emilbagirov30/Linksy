package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class SubscribeChannelUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String,channelId:Long):Response<Unit>{
        return channelRepository.subscribe(token, channelId)
    }
}