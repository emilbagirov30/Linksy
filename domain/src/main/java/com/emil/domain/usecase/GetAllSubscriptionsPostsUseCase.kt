package com.emil.domain.usecase

import com.emil.domain.model.PostResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class GetAllSubscriptionsPostsUseCase (private val feedRepository: FeedRepository){

    suspend fun execute (token:String):Response<List<PostResponse>>{
        return feedRepository.getAllSubscriptionsPosts(token)
    }
}