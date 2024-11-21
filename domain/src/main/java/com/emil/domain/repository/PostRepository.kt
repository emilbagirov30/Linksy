package com.emil.domain.repository

import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import retrofit2.Response

interface PostRepository {
    suspend fun createPost (token:String,post: PostData): Response<Unit>
    suspend fun getUserPosts (token:String): Response<List<PostResponse>>
}