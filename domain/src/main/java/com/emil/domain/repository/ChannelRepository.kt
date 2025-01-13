package com.emil.domain.repository

import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelPostData
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.PostData
import com.emil.domain.model.UserResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

}
