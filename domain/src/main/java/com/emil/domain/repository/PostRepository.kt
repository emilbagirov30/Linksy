package com.emil.domain.repository

import com.emil.domain.model.CommentData
import com.emil.domain.model.CommentResponse
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import retrofit2.Response

interface PostRepository {
    suspend fun createPost (token:String,post: PostData): Response<Unit>
    suspend fun getUserPosts (token:String): Response<List<PostResponse>>
    suspend fun getOutsiderUserPosts (token: String,id:Long): Response<List<PostResponse>>
    suspend fun deletePost (token: String,postId:Long):Response<Unit>
    suspend fun addLike (token: String,postId: Long):Response<Unit>
    suspend fun deleteLike (token: String,postId: Long):Response<Unit>
    suspend fun addComment (token:String, commentData: CommentData):Response<Unit>
    suspend fun getComments (postId: Long):Response<List<CommentResponse>>
}