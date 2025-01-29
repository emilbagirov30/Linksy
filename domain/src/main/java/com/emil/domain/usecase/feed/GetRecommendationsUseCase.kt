package com.emil.domain.usecase.feed

import com.emil.domain.model.RecommendationResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class GetRecommendationsUseCase(private val feedRepository: FeedRepository){
    suspend fun execute(token:String):Response<List<RecommendationResponse>>{
        return feedRepository.getRecommendation(token)
    }
}