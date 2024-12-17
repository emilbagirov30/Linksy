package com.emil.data.model

import com.emil.domain.model.PostData
import okhttp3.MultipartBody

data class PostBody (val text:String? = null, val image: MultipartBody.Part?=null,
                     val video: MultipartBody.Part?=null,
                     val audio: MultipartBody.Part?=null,
                     val voice: MultipartBody.Part?=null)

fun PostBody.toDomainModel (domainModel:PostData):PostBody{
    return PostBody (text = domainModel.text,
        image = domainModel.image,
        video=domainModel.video,
        audio=domainModel.audio,
        voice = domainModel.voice
    )
}