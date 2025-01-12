package com.emil.data.model

import com.emil.domain.model.ChannelPageDataResponse

data class ChannelPageDataResponseDto (
    val channelId: Long = 0,
    val ownerId: Long = 0,
    val name: String = "",
    val link: String?= null,
    val avatarUrl: String = "",
    val description: String?=null,
    val isSubscriber: Boolean = false,
    val rating: Double = 0.0,
    val type: String="",
    val subscribersCount: Long=0
)


fun ChannelPageDataResponseDto.toDomainModel ():ChannelPageDataResponse {
    return ChannelPageDataResponse(channelId, ownerId, name, link, avatarUrl, description, isSubscriber, rating, type, subscribersCount)
}