package com.emil.domain.repository

import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelResponse
import retrofit2.Response

interface ChannelRepository {
    suspend fun createChannel(token:String,channelData: ChannelData): Response<Unit>
    suspend fun getUserChannels (token:String):Response<List<ChannelResponse>>
    suspend fun getChannelPageData (token:String,channelId:Long):Response<ChannelPageDataResponse>
}