package com.emil.data.model

import com.emil.domain.model.RecommendationResponse

data class RecommendationResponseDto (val channelId:Long? =null,val userId:Long?=null,val avatarUrl:String = "null",val name:String="",val link:String?=null,val confirmed:Boolean=false)

   fun RecommendationResponseDto.toDomainModel():RecommendationResponse{
      return RecommendationResponse (channelId, userId,avatarUrl, name, link, confirmed)
   }

   fun List<RecommendationResponseDto>.toDomainModelList():List<RecommendationResponse>{
       return this.map { it.toDomainModel() }
   }