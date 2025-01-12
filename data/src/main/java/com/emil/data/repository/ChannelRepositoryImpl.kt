package com.emil.data.repository;

import com.emil.data.model.ChannelBody
import com.emil.data.model.GroupBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelResponse
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
}
