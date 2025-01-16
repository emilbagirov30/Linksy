package com.emil.data.model

import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelType
import okhttp3.MultipartBody

data class ChannelBody (val name:String="",val link:String="",val description:String="",val type:String="",
                   val avatar: MultipartBody.Part?=null)



fun ChannelBody.toDomainModel(channelData: ChannelData):ChannelData{
   return ChannelData (channelData.name, channelData.link, channelData.description, channelData.type, channelData.avatar)
}