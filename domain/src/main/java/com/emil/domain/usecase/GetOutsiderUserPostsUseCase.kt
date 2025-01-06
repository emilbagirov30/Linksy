package com.emil.domain.usecase

import com.emil.domain.model.PostResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class GetOutsiderUserPostsUseCase (private val postRepository: PostRepository) {
    suspend fun execute (id:Long): Response<List<PostResponse>> {
        return postRepository.getOutsiderUserPosts(id)
    } }
