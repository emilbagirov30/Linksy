package com.emil.domain.usecase

import com.emil.domain.model.ChannelResponse
import com.emil.domain.repository.ChannelRepository
import retrofit2.Response

class FindChannelByNameUseCase (private val channelRepository: ChannelRepository) {
    suspend fun execute (prefix:String): Response<List<ChannelResponse>> {
        return channelRepository.findChannelByName(prefix);
    }
}