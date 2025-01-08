package com.emil.data.model

import com.emil.domain.model.MessageData
import com.emil.domain.model.MomentData
import okhttp3.MultipartBody

data class MessageBody   (val recipientId:Long=0,
                          val text:String?=null,
                          val image: MultipartBody.Part?=null,
                          val video: MultipartBody.Part?=null,
                          val audio: MultipartBody.Part?=null,
                          val voice: MultipartBody.Part?=null)



fun MessageBody.toDomainModel (domainModel: MessageData):MessageBody{
    return MessageBody (
        recipientId = domainModel.recipientId,
        image = domainModel.image,
        video=domainModel.video,
        audio=domainModel.audio,
        voice = domainModel.voice,
        text = domainModel.text
    )
}

