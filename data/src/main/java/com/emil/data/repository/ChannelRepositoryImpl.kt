package com.emil.data.repository;

import com.emil.data.model.ChannelBody
import com.emil.data.model.GroupBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitCloudInstance
import com.emil.domain.model.ChannelData
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class ChannelRepositoryImpl : ChannelRepository{
    private val channelBody = ChannelBody ()
    override suspend fun createChannel(token: String, channelData: ChannelData): Response<Unit> {
        return RetrofitCloudInstance.apiService.createChannel("Bearer $token",
            channelBody.toDomainModel(channelData).name,
            channelBody.toDomainModel(channelData).link,
            channelBody.toDomainModel(channelData).description,
            channelBody.toDomainModel(channelData).type,
            channelBody.toDomainModel(channelData).avatar
            )
    }
}
