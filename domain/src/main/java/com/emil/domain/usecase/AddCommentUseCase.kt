package com.emil.domain.usecase

import com.emil.domain.model.CommentData
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class AddCommentUseCase (private val postRepository: PostRepository) {
    suspend fun execute (token:String,commentData: CommentData):Response<Unit>{
        return postRepository.addComment(token, commentData)
    }
}