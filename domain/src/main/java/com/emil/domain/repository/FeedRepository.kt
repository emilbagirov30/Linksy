package com.emil.domain.repository

import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.PostResponse
import com.emil.domain.model.RecommendationResponse
import retrofit2.Response

interface FeedRepository {
    suspend fun getAllChannelsPosts (token:String):Response<List<ChannelPostResponse>>
    suspend fun getAllSubscriptionsPosts (token:String):Response<List<PostResponse>>
    suspend fun getRecommendation(token:String) :Response<List<RecommendationResponse>>
}