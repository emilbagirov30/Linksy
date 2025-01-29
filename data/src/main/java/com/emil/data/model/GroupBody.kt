package com.emil.data.model

import com.emil.domain.model.GroupData
import okhttp3.MultipartBody

class GroupBody(val participantIds:String="",
                val name:String="", val avatar: MultipartBody.Part?=null)

fun GroupBody.toDomainModel (domainModel:GroupData):GroupBody{
    return GroupBody (
        participantIds = domainModel.participantIds,
        name = domainModel.name,
        avatar=domainModel.avatar
    )
}