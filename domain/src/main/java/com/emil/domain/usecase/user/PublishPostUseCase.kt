package com.emil.domain.usecase.user

import com.emil.domain.model.PostData
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class PublishPostUseCase (private val postRepository: PostRepository) {
    suspend fun execute (token:String,postData: PostData):Response<Unit>{
        return postRepository.createPost(token,postData)
    }
}