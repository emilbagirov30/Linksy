package com.emil.domain.usecase

import com.emil.domain.model.CommentData
import com.emil.domain.repository.ChannelRepository
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class AddChannelPostCommentUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (token:String,commentData: CommentData): Response<Unit> {
        return channelRepository.addComment(token, commentData)
    }
}

