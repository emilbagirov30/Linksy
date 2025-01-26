package com.emil.data.repository

import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.PostResponse
import com.emil.domain.model.RecommendationResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class FeedRepositoryImpl: FeedRepository {
    override suspend fun getAllChannelsPosts(token: String): Response<List<ChannelPostResponse>> {
        val response =RetrofitInstance.apiService.getAllChannelsPost("Bearer $token")
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun getAllSubscriptionsPosts(token: String): Response<List<PostResponse>> {
        val response = RetrofitInstance.apiService.getAllSubscriptionsPosts("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getRecommendation(token:String): Response<List<RecommendationResponse>> {
        val response = RetrofitInstance.apiService.getRecommendation("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}