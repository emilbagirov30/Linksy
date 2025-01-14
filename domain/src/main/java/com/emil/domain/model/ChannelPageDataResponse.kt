package com.emil.domain.model

data class ChannelPageDataResponse (
    val channelId: Long,
    val ownerId: Long,
    val name: String,
    val link: String?,
    val avatarUrl: String,
    val description: String?,
    val isSubscriber: Boolean,
    val rating: Double,
    val type: String,
    val subscribersCount: Long,
    val requestsCount:Long
)