package com.emil.domain.usecase

import com.emil.domain.model.UnseenSubscriptionMomentResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class GetAllUnseenMomentsUseCase (private val feedRepository: FeedRepository){
    suspend fun execute(token:String):Response<List<UnseenSubscriptionMomentResponse>>{
        return feedRepository.getAllUnseenMoments(token)
    }
}