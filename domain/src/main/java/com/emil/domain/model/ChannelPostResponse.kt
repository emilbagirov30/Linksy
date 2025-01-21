package com.emil.domain.model

class ChannelPostResponse (val postId: Long, val channelName: String, val channelAvatarUrl: String, val text: String?, val imageUrl: String?,
val videoUrl: String?, val audioUrl: String?, val publishDate: String, val pollTitle: String?,val isVoted:Boolean, val options: List<OptionResponse>?, val averageRating: Double, val repostsCount: Long,
    val edited:Boolean,val authorId:Long,val commentsCount:Long,val isAppreciated:Boolean
)



