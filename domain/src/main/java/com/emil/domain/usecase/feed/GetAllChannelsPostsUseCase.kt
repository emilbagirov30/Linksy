package com.emil.domain.usecase.feed

import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.repository.FeedRepository
import retrofit2.Response

class GetAllChannelsPostsUseCase (private val feedRepository: FeedRepository){

    suspend fun execute (token:String): Response<List<ChannelPostResponse>> {
        return feedRepository.getAllChannelsPosts(token)
    }
}