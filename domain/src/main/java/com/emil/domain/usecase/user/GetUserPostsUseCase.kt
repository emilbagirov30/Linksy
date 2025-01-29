package com.emil.domain.usecase.user

import com.emil.domain.model.PostResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class GetUserPostsUseCase (private val postRepository: PostRepository) {
    suspend fun execute (token:String): Response<List<PostResponse>> {
        return postRepository.getUserPosts(token)
    }
}