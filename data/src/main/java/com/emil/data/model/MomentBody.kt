package com.emil.data.model

import com.emil.domain.model.MomentData
import okhttp3.MultipartBody

data class MomentBody(val image: MultipartBody.Part?=null,
                      val video: MultipartBody.Part?=null,
                      val audio: MultipartBody.Part?=null,
                      val text:String?=null)



fun MomentBody.toDomainModel (domainModel: MomentData):MomentBody{
    return MomentBody (
        image = domainModel.image,
        video= domainModel.video,
        audio= domainModel.audio,
        text = domainModel.text
    )
}