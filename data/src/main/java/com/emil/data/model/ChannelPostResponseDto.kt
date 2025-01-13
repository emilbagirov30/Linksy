package com.emil.data.model

import com.emil.domain.model.ChannelPostResponse

class ChannelPostResponseDto (val postId: Long=0, val channelName: String="", val channelAvatarUrl: String="", val text: String="", val imageUrl: String="",
                              val videoUrl: String="", val audioUrl: String="", val publishDate: String="", val pollTitle: String="", val options: List<OptionResponseDto> = emptyList(),
                              val averageRating: Double=0.0, val repostsCount: Long = 0)






fun ChannelPostResponseDto.toDomainModel ():ChannelPostResponse{
    return ChannelPostResponse(postId, channelName, channelAvatarUrl, text, imageUrl, videoUrl, audioUrl,
        publishDate, pollTitle, options.toDomainModelList(), averageRating, repostsCount)
}


      fun List<ChannelPostResponseDto>.toDomainModelList ():List<ChannelPostResponse>{
          return this.map { it.toDomainModel() }
      }