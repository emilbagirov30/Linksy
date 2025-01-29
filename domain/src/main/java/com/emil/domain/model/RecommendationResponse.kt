package com.emil.domain.model

data class RecommendationResponse (val channelId:Long?,
                                   val userId:Long?,
                                   val avatarUrl:String,
                                   val name:String,
                                   val link:String?,
                                   val confirmed:Boolean)