package com.emil.data.repository

import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.PostResponse
import com.emil.domain.model.RecommendationResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class FeedRepositoryImpl: FeedRepository {
    override suspend fun getAllChannelsPosts(token: String): Response<List<ChannelPostResponse>> {
        val response =RetrofitUserInstance.apiService.getAllChannelsPost("Bearer $token")
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getAllSubscriptionsPosts(token: String): Response<List<PostResponse>> {
        val response = RetrofitUserInstance.apiService.getAllSubscriptionsPosts("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getRecommendation(token:String): Response<List<RecommendationResponse>> {
        val response = RetrofitUserInstance.apiService.getRecommendation("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}