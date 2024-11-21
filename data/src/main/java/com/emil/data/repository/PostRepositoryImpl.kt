package com.emil.data.repository

import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.PostData
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class PostRepositoryImpl:PostRepository {
    private val postBody = PostBody ()
    override suspend fun createPost(token: String, post: PostData):Response<Unit> {
        return RetrofitInstance.apiService.createPost("Bearer $token", postBody = postBody.toDomainModel(post))
    }
}