package com.emil.data.repository

import com.emil.data.model.CommentBody
import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.CommentData
import com.emil.domain.model.CommentResponse
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class PostRepositoryImpl:PostRepository {
    private val postBody = PostBody ()
    private val commentBody = CommentBody ()
    override suspend fun createPost(token: String, post: PostData):Response<Unit> {
        return RetrofitInstance.apiService.createOrUpdatePost("Bearer $token",
            postBody.toDomainModel(post).postId,
            postBody.toDomainModel(post).text,
            postBody.toDomainModel(post).oldImageUrl,
            postBody.toDomainModel(post).oldVideoUrl,
            postBody.toDomainModel(post).oldAudioUrl,
            postBody.toDomainModel(post).oldVoiceIrl,
            postBody.toDomainModel(post).image,
            postBody.toDomainModel(post).video,
            postBody.toDomainModel(post).audio,
            postBody.toDomainModel(post).voice
            )
    }

    override suspend fun getUserPosts(token: String): Response<List<PostResponse>> {
       val response = RetrofitInstance.apiService.getUserPosts("Bearer $token")
        return if (response.isSuccessful) {
              Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderUserPosts(token: String,id: Long): Response<List<PostResponse>> {
        val response = RetrofitInstance.apiService.getOutsiderUserPosts("Bearer $token",id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun deletePost(token: String, postId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deletePost("Bearer $token",postId)
    }

    override suspend fun addLike(token: String, postId: Long): Response<Unit> {
        return RetrofitInstance.apiService.addLike("Bearer $token",postId)
    }

    override suspend fun deleteLike(token: String, postId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deleteLike("Bearer $token",postId)
    }

    override suspend fun addComment(token: String, commentData: CommentData): Response<Unit> {
        return RetrofitInstance.apiService.addComment("Bearer $token", commentBody.toDomainModel(commentData))
    }

    override suspend fun getComments(postId: Long): Response<List<CommentResponse>> {
        val response = RetrofitInstance.apiService.getPostComments(postId)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun deleteComment(token: String, commentId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deleteComment("Bearer $token",commentId)
    }

    override suspend fun getPostLikes(token: String, postId: Long): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.getPostLikes("Bearer $token",postId)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}