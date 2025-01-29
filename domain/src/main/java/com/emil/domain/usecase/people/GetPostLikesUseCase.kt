package com.emil.domain.usecase.people

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class GetPostLikesUseCase (private val postRepository:PostRepository){
    suspend fun execute (token:String,postId:Long): Response<List<UserResponse>> {
         return postRepository.getPostLikes(token, postId)
    }
}