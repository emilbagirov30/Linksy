package com.emil.data.repository;

import com.emil.data.model.ChannelBody
import com.emil.data.model.ChannelPostBody
import com.emil.data.model.CommentBody
import com.emil.data.model.GroupBody
import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
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
        return RetrofitCloudInstance.apiService.createOrUpdateChannel("Bearer $token",
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
        val response = RetrofitUserInstance.apiService.getChannels("Bearer $token")
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getChannelPageData(token: String, channelId: Long): Response<ChannelPageDataResponse> {
        val response = RetrofitUserInstance.apiService.getChannelPageData("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModel())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun createPost(token: String, postData: ChannelPostData): Response<Unit> {
        return RetrofitCloudInstance.apiService.createOrUpdateChannelPost("Bearer $token",
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
        val response =RetrofitUserInstance.apiService.getChannelsPost("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun submitRequest(token: String, channelId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.submitRequest("Bearer $token",channelId)
    }

    override suspend fun deleteRequest(token: String, channelId: Long): Response<Unit> {
      return  RetrofitUserInstance.apiService.deleteRequest("Bearer $token",channelId)
    }

    override suspend fun getChannelSubscriptionRequests(token: String, channelId: Long): Response<List<UserResponse>> {
        val response =  RetrofitUserInstance.apiService.getChannelSubscriptionRequests("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun acceptUserToChannel(token: String, channelId: Long, candidateId: Long): Response<Unit> {
       return RetrofitUserInstance.apiService.acceptUserToChannel("Bearer $token",channelId, candidateId)
    }

    override suspend fun rejectSubscriptionRequest(token: String, channelId: Long, candidateId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.rejectSubscriptionRequest("Bearer $token",channelId, candidateId)
    }

    override suspend fun getChannelMembers(token: String, channelId: Long): Response<List<UserResponse>> {
        val response =  RetrofitUserInstance.apiService.getChannelMembers("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun deleteChannelPost(token: String, channelId: Long): Response<Unit> {
      return RetrofitUserInstance.apiService.deleteChannelPost("Bearer $token", channelId)
    }

    override suspend fun subscribe(token: String, channelId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.subscribeChannel("Bearer $token",channelId)
    }

    override suspend fun unsubscribe(token: String, channelId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.unsubscribeChannel("Bearer $token",channelId)
    }

    override suspend fun vote(token: String, optionId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.vote("Bearer $token",optionId)
    }

    override suspend fun findChannelByName(prefix: String): Response<List<ChannelResponse>> {
       val response =  RetrofitUserInstance.apiService.findChannelByName(prefix);
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
            else Response.error(response.code(), response.errorBody()!!)

    }

    override suspend fun findChannelByLink(prefix: String): Response<List<ChannelResponse>> {
        val response =  RetrofitUserInstance.apiService.findChannelByLink(prefix);
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getChannelManagementData(token: String, channelId: Long, ): Response<ChannelManagementResponse> {
        val response =  RetrofitUserInstance.apiService.getChannelManagementData("Bearer $token",channelId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModel())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun addScore(token: String, postId: Long, score: Int): Response<Unit> {
        return RetrofitUserInstance.apiService.addScore("Bearer $token",postId,score)
    }

    override suspend fun deleteScore(token: String, postId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.deleteScore("Bearer $token",postId)
    }

    override suspend fun addComment(token: String, commentData: CommentData): Response<Unit> {
        return RetrofitUserInstance.apiService.addChannelPostComment("Bearer $token", commentBody.toDomainModel(commentData))
    }

    override suspend fun getComments(postId: Long): Response<List<CommentResponse>> {
            val response = RetrofitUserInstance.apiService.getChannelPostComments(postId)
            return if (response.isSuccessful) {
                Response.success(response.body()?.toDomainModelList())
            } else {
                Response.error(response.code(), response.errorBody()!!)
            }
    }

    override suspend fun deleteComment(token: String, commentId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.deleteChannelComment("Bearer $token",commentId)
    }

    override suspend fun getPostAppreciated(token: String, postId: Long): Response<List<PostAppreciatedResponse>> {
      val response =  RetrofitUserInstance.apiService.getPostAppreciated("Bearer $token",postId)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}
