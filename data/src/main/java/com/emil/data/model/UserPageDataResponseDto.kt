package com.emil.data.model

import com.emil.domain.model.MessageMode
import com.emil.domain.model.UserPageDataResponse

class UserPageDataResponseDto (val username:String="",
                               val link:String?=null,
                               val avatarUrl:String="",
                               val birthday:String?=null,
                               val isSubscriber:Boolean = false,
                               val subscriptionsCount:Long=0,
                               val subscribersCount:Long=0,
                               val isPageOwnerBlockedByViewer:Boolean = false,
                               val messageMode: MessageMode = MessageMode.NOBODY,
                               val isSubscription:Boolean = false,
                               val confirmed:Boolean = false,
                               val online:Boolean=false,
                               val lastActive:String=""
)


    fun UserPageDataResponseDto.toDomainModel():UserPageDataResponse{
       return UserPageDataResponse(username, link, avatarUrl, birthday,isSubscriber,subscriptionsCount,
           subscribersCount, isPageOwnerBlockedByViewer,messageMode,isSubscription,confirmed, online, lastActive)
    }

