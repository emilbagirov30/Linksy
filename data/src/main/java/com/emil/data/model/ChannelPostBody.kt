package com.emil.data.model

import com.emil.domain.model.ChannelPostData
import okhttp3.MultipartBody

class ChannelPostBody (val channelId:Long=0,
                       val text:String? = "",
                       val postId: Long?=null,
                       val imageUrl: String?=null,
                       val videoUrl: String?=null,
                       val audioUrl: String?=null,
                       val image: MultipartBody.Part?=null,
                       val video: MultipartBody.Part?=null,
                       val audio: MultipartBody.Part?=null,
                       val pollTitle: String?=null,
                       val options: List<String> = emptyList())


fun ChannelPostBody.toDomainModel (domainModel: ChannelPostData):ChannelPostBody{
    return ChannelPostBody (
        channelId = domainModel.channelId,
        text = domainModel.text,
        postId = domainModel.postId,
        imageUrl = domainModel.imageUrl,
        videoUrl = domainModel.videoUrl,
        audioUrl = domainModel.audioUrl,
        image = domainModel.image,
        video=domainModel.video,
        audio=domainModel.audio,
        pollTitle = domainModel.pollTitle,
        options = domainModel.options
    )
}