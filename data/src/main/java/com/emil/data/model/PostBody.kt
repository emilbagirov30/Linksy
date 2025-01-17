package com.emil.data.model

import com.emil.domain.model.PostData
import okhttp3.MultipartBody

data class PostBody (val postId:Long?=null,val text:String? = null,
                     val oldImageUrl:String? = null,
                     val oldVideoUrl:String?=null,
                     val oldAudioUrl:String?=null,
                     val oldVoiceIrl:String?=null,
                     val image: MultipartBody.Part?=null,
                     val video: MultipartBody.Part?=null,
                     val audio: MultipartBody.Part?=null,
                     val voice: MultipartBody.Part?=null)

fun PostBody.toDomainModel (domainModel:PostData):PostBody{
    return PostBody (
        postId = domainModel.postId,
        text = domainModel.text,
        oldImageUrl = domainModel.oldImageUrl,
        oldVideoUrl = domainModel.oldVideoUrl,
        oldAudioUrl = domainModel.oldAudioUrl,
        oldVoiceIrl = domainModel.oldVoiceIrl,
        image = domainModel.image,
        video=domainModel.video,
        audio=domainModel.audio,
        voice = domainModel.voice
    )
}