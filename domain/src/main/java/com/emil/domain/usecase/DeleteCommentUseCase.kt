package com.emil.domain.usecase

import com.emil.domain.repository.PostRepository
import retrofit2.Response

class DeleteCommentUseCase  (private val postRepository: PostRepository) {
    suspend fun execute (token:String,commentId:Long):Response<Unit>{
        return postRepository.deleteComment(token, commentId)
    }
}