package com.emil.domain.usecase.channel;

import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class GetChannelPageDataUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String,channelId:Long):Response<ChannelPageDataResponse>{
        return channelRepository.getChannelPageData(token, channelId)
    }
}
