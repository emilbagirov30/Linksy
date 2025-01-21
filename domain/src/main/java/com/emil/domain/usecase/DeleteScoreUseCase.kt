package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class DeleteScoreUseCase  (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String,postId:Long): Response<Unit> {
        return channelRepository.deleteScore(token, postId)
    }
}