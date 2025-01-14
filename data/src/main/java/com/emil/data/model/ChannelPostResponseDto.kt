package com.emil.data.model

import com.emil.domain.model.ChannelPostResponse

class ChannelPostResponseDto (val postId: Long=0, val channelName: String="", val channelAvatarUrl: String="", val text: String?=null, val imageUrl: String? = null,
                              val videoUrl: String?=null, val audioUrl: String?=null, val publishDate: String="", val pollTitle: String?=null, val isVoted:Boolean,val options: List<OptionResponseDto>? = null,
                              val averageRating: Double=0.0, val repostsCount: Long = 0)






fun ChannelPostResponseDto.toDomainModel ():ChannelPostResponse{
    return ChannelPostResponse(postId, channelName, channelAvatarUrl, text, imageUrl, videoUrl, audioUrl,
        publishDate, pollTitle,isVoted, options?.toDomainModelList(), averageRating, repostsCount)
}


      fun List<ChannelPostResponseDto>.toDomainModelList ():List<ChannelPostResponse>{
          return this.map { it.toDomainModel() }
      }