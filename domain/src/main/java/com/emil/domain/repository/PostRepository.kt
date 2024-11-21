package com.emil.domain.repository

import com.emil.domain.model.PostData
import retrofit2.Response

interface PostRepository {
    suspend fun createPost (token:String,post: PostData): Response<Unit>
}