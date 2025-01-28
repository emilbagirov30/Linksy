package com.emil.data.model

import com.emil.domain.model.UnseenSubscriptionMomentResponse

class UnseenSubscriptionMomentResponseDto (val id:Long=0, val avatarUrl:String="", val username:String="", val confirmed:Boolean=false)

   fun UnseenSubscriptionMomentResponseDto.toDomainModel():UnseenSubscriptionMomentResponse{
       return UnseenSubscriptionMomentResponse(id, avatarUrl, username, confirmed)
   }

fun List<UnseenSubscriptionMomentResponseDto>.toDomainModelList():List<UnseenSubscriptionMomentResponse>{
    return this.map { it.toDomainModel() }
}