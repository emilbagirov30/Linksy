package com.emil.domain.usecase

import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class GetChannelPostsUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute(token:String,channelId:Long,): Response<List<ChannelPostResponse>> {
        return channelRepository.getChannelPosts(token, channelId)
    }
}