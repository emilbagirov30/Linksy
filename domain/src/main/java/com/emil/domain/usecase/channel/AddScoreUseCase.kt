package com.emil.domain.usecase.channel

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class AddScoreUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String,postId:Long,score:Int):Response<Unit>{
        return channelRepository.addScore(token, postId, score)
    }
}