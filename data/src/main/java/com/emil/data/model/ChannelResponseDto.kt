package com.emil.data.model

import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChannelType

data class ChannelResponseDto (val channelId:Long=0, val ownerId:Long=0, val name:String="", val link:String?="", val avatarUrl:String="", val rating:Double=0.0,
                               val type: ChannelType = ChannelType.PRIVATE,val confirmed:Boolean)

fun ChannelResponseDto.toDomainModel ():ChannelResponse{
    return ChannelResponse(channelId, ownerId, name, link, avatarUrl, rating, type,confirmed)
}



fun List<ChannelResponseDto>.toDomainModelList():List<ChannelResponse>{
    return this.map { it.toDomainModel() }
}