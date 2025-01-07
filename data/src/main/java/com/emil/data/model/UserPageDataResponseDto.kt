package com.emil.data.model

import com.emil.domain.model.UserPageDataResponse

class UserPageDataResponseDto (val username:String="",val link:String?=null,val avatarUrl:String="",val birthday:String?=null,val isSubscriber:Boolean = false,val subscriptionsCount:Long=0,
                               val subscribersCount:Long=0)


    fun UserPageDataResponseDto.toDomainModel():UserPageDataResponse{
       return UserPageDataResponse(username, link, avatarUrl, birthday,isSubscriber,subscriptionsCount, subscribersCount)
    }

