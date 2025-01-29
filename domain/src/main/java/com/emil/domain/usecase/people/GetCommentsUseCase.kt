package com.emil.domain.usecase.people

import com.emil.domain.model.CommentResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class GetCommentsUseCase (private val postRepository: PostRepository) {
    suspend fun execute (postId:Long): Response<List<CommentResponse>> {
        return postRepository.getComments(postId)
    }
}