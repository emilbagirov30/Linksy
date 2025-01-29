package com.emil.data.model

import com.emil.domain.model.ChannelData
import okhttp3.MultipartBody

data class ChannelBody (val name:String="",
                        val channelId:Long? = null,
                        val link:String="",
                        val description:String="",
                        val type:String="",
                        val oldAvatarUrl:String? = null,
                        val avatar: MultipartBody.Part?=null)



fun ChannelBody.toDomainModel(channelData: ChannelData):ChannelData{
   return ChannelData(
       channelData.name,
       channelData.channelId,
       channelData.link,
       channelData.description,
       channelData.type,
       channelData.oldAvatarUrl,
       channelData.avatar)
}