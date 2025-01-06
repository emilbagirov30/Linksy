package com.emil.data.repository

import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import com.emil.domain.repository.PostRepository
import retrofit2.Response

class PostRepositoryImpl:PostRepository {
    private val postBody = PostBody ()
    override suspend fun createPost(token: String, post: PostData):Response<Unit> {
        return RetrofitCloudInstance.apiService.createPost("Bearer $token",
            postBody.toDomainModel(post).text,
            postBody.toDomainModel(post).image,
            postBody.toDomainModel(post).video,
            postBody.toDomainModel(post).audio,
            postBody.toDomainModel(post).voice
            )
    }

    override suspend fun getUserPosts(token: String): Response<List<PostResponse>> {
       val response = RetrofitUserInstance.apiService.getUserPosts("Bearer $token")
        return if (response.isSuccessful) {
              Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderUserPosts(id: Long): Response<List<PostResponse>> {
        val response = RetrofitUserInstance.apiService.getOutsiderUserPosts(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun deletePost(token: String, postId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.deletePost("Bearer $token",postId)
    }
}