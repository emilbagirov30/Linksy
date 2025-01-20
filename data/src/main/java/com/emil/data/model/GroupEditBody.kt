package com.emil.data.model

import com.emil.domain.model.GroupData
import com.emil.domain.model.GroupEditData
import okhttp3.MultipartBody

class GroupEditBody (val groupId:Long = 0,val name:String="", val oldAvatarUrl:String?=null,val avatar: MultipartBody.Part?=null)




fun GroupEditBody.toDomainModel (domainModel: GroupEditData):GroupEditBody{
    return GroupEditBody (
        groupId = domainModel.groupId,
        name = domainModel.name,
        oldAvatarUrl = domainModel.oldAvatarUrl,
        avatar = domainModel.avatar
    )
}