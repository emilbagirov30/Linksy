package com.emil.domain.repository

import com.emil.domain.model.ChannelData
import retrofit2.Response

interface ChannelRepository {
    suspend fun createChannel(token:String,channelData: ChannelData): Response<Unit>
}