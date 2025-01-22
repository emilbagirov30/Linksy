package com.emil.domain.usecase

import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class DeleteChannelCommentUseCase (private val channelRepository: ChannelRepository){
    suspend fun execute (token:String,commentId:Long): Response<Unit> {
        return channelRepository.deleteComment(token, commentId)
    }
}