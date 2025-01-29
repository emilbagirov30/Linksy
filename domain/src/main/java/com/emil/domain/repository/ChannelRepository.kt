package com.emil.domain.repository

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
import retrofit2.Response



interface ChannelRepository {
    suspend fun createChannel(token:String,channelData: ChannelData): Response<Unit>
    suspend fun getUserChannels (token:String):Response<List<ChannelResponse>>
    suspend fun getChannelPageData (token:String,channelId:Long):Response<ChannelPageDataResponse>
    suspend fun createPost (token:String,postData: ChannelPostData):Response<Unit>
    suspend fun getChannelPosts(token:String,channelId: Long):Response<List<ChannelPostResponse>>
    suspend fun submitRequest(token:String,channelId: Long):Response<Unit>
    suspend fun deleteRequest(token:String,channelId: Long):Response<Unit>
    suspend fun getChannelSubscriptionRequests(token: String, channelId: Long): Response<List<UserResponse>>
    suspend fun acceptUserToChannel(token: String, channelId: Long, candidateId: Long): Response<Unit>
    suspend fun rejectSubscriptionRequest(token: String,channelId: Long, candidateId: Long): Response<Unit>
    suspend fun getChannelMembers(token: String, channelId: Long): Response<List<UserResponse>>
    suspend fun deleteChannelPost(token: String, channelId: Long): Response<Unit>
    suspend fun subscribe(token: String, channelId: Long): Response<Unit>
    suspend fun unsubscribe(token: String, channelId: Long): Response<Unit>
    suspend fun vote(token: String, optionId: Long): Response<Unit>
    suspend fun findChannelByName (prefix:String): Response<List<ChannelResponse>>
    suspend fun findChannelByLink (prefix:String): Response<List<ChannelResponse>>
    suspend fun getChannelManagementData(token: String, channelId: Long): Response<ChannelManagementResponse>
    suspend fun addScore(token: String, postId: Long,score:Int): Response<Unit>
    suspend fun deleteScore(token: String, postId: Long): Response<Unit>
    suspend fun addComment (token:String, commentData: CommentData):Response<Unit>
    suspend fun getComments (postId: Long):Response<List<CommentResponse>>
    suspend fun deleteComment (token: String,commentId: Long):Response<Unit>
    suspend fun getPostAppreciated (token: String,postId: Long):Response<List<PostAppreciatedResponse>>
}
