package com.emil.data.model

import com.emil.domain.model.ChannelPostResponse

class ChannelPostResponseDto (val postId: Long=0,
                              val channelId:Long =0,
                              val channelName: String="",
                              val confirmed:Boolean = false,
                              val channelAvatarUrl: String="",
                              val text: String?=null,
                              val imageUrl: String? = null,
                              val videoUrl: String?=null,
                              val audioUrl: String?=null,
                              val publishDate: String="",
                              val pollTitle: String?=null,
                              val isVoted:Boolean,
                              val options: List<OptionResponseDto>? = null,
                              val averageRating: Double=0.0,
                              val edited:Boolean = false,
                              val authorId:Long=0,
                              val commentsCount:Long=0,
                              val userScore:Int? = null)






fun ChannelPostResponseDto.toDomainModel ():ChannelPostResponse{
    return ChannelPostResponse(postId,channelId, channelName, confirmed = confirmed,channelAvatarUrl, text, imageUrl, videoUrl, audioUrl,
        publishDate, pollTitle,isVoted, options?.toDomainModelList(), averageRating,edited,authorId, commentsCount, userScore)
}


      fun List<ChannelPostResponseDto>.toDomainModelList ():List<ChannelPostResponse>{
          return this.map { it.toDomainModel() }
      }