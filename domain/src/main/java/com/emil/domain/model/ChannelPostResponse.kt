package com.emil.domain.model

data class ChannelPostResponse(
     val postId: Long,
     val channelId:Long,
     val channelName: String,
     val confirmed:Boolean,
     val channelAvatarUrl: String,
     val text: String?,
     val imageUrl: String?,
     val videoUrl: String?,
     val audioUrl: String?,
     val publishDate: String,
     val pollTitle: String?,
     val isVoted:Boolean,
     val options: List<OptionResponse>?,
     val averageRating: Double,
     val edited:Boolean,
     val authorId:Long,
     val commentsCount:Long,
     val userScore:Int?
)



