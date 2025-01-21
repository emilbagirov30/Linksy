package com.emil.domain.usecase

import com.emil.domain.model.CommentResponse
import com.emil.domain.repository.ChannelRepository
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class GetChannelPostCommentsUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (postId:Long): Response<List<CommentResponse>> {
        return channelRepository.getComments(postId)
    }
}