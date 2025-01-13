package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class VoteUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (token:String,optionId:Long):Response<Unit>{
        return channelRepository.vote(token, optionId)
    }
}