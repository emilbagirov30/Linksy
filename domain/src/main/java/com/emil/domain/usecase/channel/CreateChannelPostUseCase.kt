package com.emil.domain.usecase.channel

import com.emil.domain.model.ChannelPostData
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class CreateChannelPostUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (token:String,channelPostData: ChannelPostData): Response<Unit> {
        return channelRepository.createPost(token,channelPostData)
    }
}