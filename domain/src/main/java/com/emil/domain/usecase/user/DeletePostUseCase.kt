package com.emil.domain.usecase.user

import com.emil.domain.repository.PostRepository
import retrofit2.Response

class DeletePostUseCase (private val postRepository: PostRepository){
    suspend fun execute (token:String,postId:Long):Response<Unit>{
        return postRepository.deletePost(token,postId)
    }
}