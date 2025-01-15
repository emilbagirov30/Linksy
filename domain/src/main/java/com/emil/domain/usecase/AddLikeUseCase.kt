package com.emil.domain.usecase

import com.emil.domain.model.CommentData
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class AddLikeUseCase (private val postRepository: PostRepository) {
    suspend fun execute (token:String,postId:Long): Response<Unit> {
        return postRepository.addLike(token, postId)
    }
}