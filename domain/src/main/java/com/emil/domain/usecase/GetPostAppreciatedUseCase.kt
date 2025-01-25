package com.emil.domain.usecase

import com.emil.domain.model.PostAppreciatedResponse
import com.emil.domain.model.Token
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class GetPostAppreciatedUseCase(private val channelRepository: ChannelRepository) {
    suspend fun execute (token: String,postId:Long):Response<List<PostAppreciatedResponse>>{
        return  channelRepository.getPostAppreciated(token, postId)
    }
}