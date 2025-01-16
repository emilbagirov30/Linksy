package com.emil.data.model

import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelType

data class ChannelPageDataResponseDto (
    val channelId: Long = 0,
    val ownerId: Long = 0,
    val name: String = "",
    val link: String?= null,
    val avatarUrl: String = "",
    val description: String?=null,
    val isSubscriber: Boolean = false,
    val isSubmitted: Boolean = false,
    val rating: Double = 0.0,
    val type: ChannelType = ChannelType.PRIVATE,
    val subscribersCount: Long=0,
    val requestsCount:Long = 0
)


fun ChannelPageDataResponseDto.toDomainModel ():ChannelPageDataResponse {
    return ChannelPageDataResponse(channelId, ownerId, name, link, avatarUrl, description, isSubscriber,isSubmitted, rating, type, subscribersCount,requestsCount)
}