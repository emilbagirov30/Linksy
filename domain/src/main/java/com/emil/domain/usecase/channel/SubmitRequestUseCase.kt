package com.emil.domain.usecase.channel

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class SubmitRequestUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long):Response<Unit>{
        return channelRepository.submitRequest(token, channelId)
    }
}