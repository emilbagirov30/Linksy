package com.emil.data.repository;

import com.emil.data.model.ChannelBody
import com.emil.data.model.ChannelPostBody
import com.emil.data.model.CommentBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelManagementResponse
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelPostData
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.CommentData
import com.emil.domain.model.CommentResponse
import com.emil.domain.model.PostAppreciatedResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class ChannelRepositoryImpl : ChannelRepository{
    private val channelBody = ChannelBody ()
    private val postBody = ChannelPostBody ()
    private val commentBody = CommentBody ()
    override suspend fun createChannel(token: String, channelData: ChannelData): Response<Unit> {
        return RetrofitInstance.apiService.createOrUpdateChannel("Bearer $token",
            channelBody.toDomainModel(channelData).name,
            channelBody.toDomainModel(channelData).channelId,
            channelBody.toDomainModel(channelData).link,
            channelBody.toDomainModel(channelData).description,
            channelBody.toDomainModel(channelData).type,
            channelBody.toDomainModel(channelData).oldAvatarUrl,
            channelBody.toDomainModel(channelData).avatar
            )
    }

    override suspend fun getUserChannels(token: String): Response<List<ChannelResponse>> {
        val response = RetrofitInstance.apiService.getChannels("Bearer $token")
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getChannelPageData(token: String, channelId: Long): Response<ChannelPageDataResponse> {
        val response = RetrofitInstance.apiService.getChannelPageData("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModel())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun createPost(token: String, postData: ChannelPostData): Response<Unit> {
        return RetrofitInstance.apiService.createOrUpdateChannelPost("Bearer $token",
            postBody.toDomainModel(postData).channelId,
            postBody.toDomainModel(postData).text,
            postBody.toDomainModel(postData).postId,
            postBody.toDomainModel(postData).imageUrl,
            postBody.toDomainModel(postData).videoUrl,
            postBody.toDomainModel(postData).audioUrl,
            postBody.toDomainModel(postData).image,
            postBody.toDomainModel(postData).video,
            postBody.toDomainModel(postData).audio,
            postBody.toDomainModel(postData).pollTitle,
            postBody.toDomainModel(postData).options
        )
    }

    override suspend fun getChannelPosts(token: String, channelId: Long): Response<List<ChannelPostResponse>> {
        val response =RetrofitInstance.apiService.getChannelsPost("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun submitRequest(token: String, channelId: Long): Response<Unit> {
        return RetrofitInstance.apiService.submitRequest("Bearer $token",channelId)
    }

    override suspend fun deleteRequest(token: String, channelId: Long): Response<Unit> {
      return  RetrofitInstance.apiService.deleteRequest("Bearer $token",channelId)
    }

    override suspend fun getChannelSubscriptionRequests(token: String, channelId: Long): Response<List<UserResponse>> {
        val response =  RetrofitInstance.apiService.getChannelSubscriptionRequests("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun acceptUserToChannel(token: String, channelId: Long, candidateId: Long): Response<Unit> {
       return RetrofitInstance.apiService.acceptUserToChannel("Bearer $token",channelId, candidateId)
    }

    override suspend fun rejectSubscriptionRequest(token: String, channelId: Long, candidateId: Long): Response<Unit> {
        return RetrofitInstance.apiService.rejectSubscriptionRequest("Bearer $token",channelId, candidateId)
    }

    override suspend fun getChannelMembers(token: String, channelId: Long): Response<List<UserResponse>> {
        val response =  RetrofitInstance.apiService.getChannelMembers("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun deleteChannelPost(token: String, channelId: Long): Response<Unit> {
      return RetrofitInstance.apiService.deleteChannelPost("Bearer $token", channelId)
    }

    override suspend fun subscribe(token: String, channelId: Long): Response<Unit> {
        return RetrofitInstance.apiService.subscribeChannel("Bearer $token",channelId)
    }

    override suspend fun unsubscribe(token: String, channelId: Long): Response<Unit> {
        return RetrofitInstance.apiService.unsubscribeChannel("Bearer $token",channelId)
    }

    override suspend fun vote(token: String, optionId: Long): Response<Unit> {
        return RetrofitInstance.apiService.vote("Bearer $token",optionId)
    }

    override suspend fun findChannelByName(prefix: String): Response<List<ChannelResponse>> {
       val response =  RetrofitInstance.apiService.findChannelByName(prefix);
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
            else Response.error(response.code(), response.errorBody()!!)

    }

    override suspend fun findChannelByLink(prefix: String): Response<List<ChannelResponse>> {
        val response =  RetrofitInstance.apiService.findChannelByLink(prefix);
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getChannelManagementData(token: String, channelId: Long, ): Response<ChannelManagementResponse> {
        val response =  RetrofitInstance.apiService.getChannelManagementData("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModel())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun addScore(token: String, postId: Long, score: Int): Response<Unit> {
        return RetrofitInstance.apiService.addScore("Bearer $token",postId,score)
    }

    override suspend fun deleteScore(token: String, postId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deleteScore("Bearer $token",postId)
    }

    override suspend fun addComment(token: String, commentData: CommentData): Response<Unit> {
        return RetrofitInstance.apiService.addChannelPostComment("Bearer $token", commentBody.toDomainModel(commentData))
    }

    override suspend fun getComments(postId: Long): Response<List<CommentResponse>> {
            val response = RetrofitInstance.apiService.getChannelPostComments(postId)
            return if (response.isSuccessful) {
                Response.success(response.body()?.toDomainModelList())
            } else {
                Response.error(response.code(), response.errorBody()!!)
            }
    }

    override suspend fun deleteComment(token: String, commentId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deleteChannelComment("Bearer $token",commentId)
    }

    override suspend fun getPostAppreciated(token: String, postId: Long): Response<List<PostAppreciatedResponse>> {
      val response =  RetrofitInstance.apiService.getPostAppreciated("Bearer $token",postId)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}
