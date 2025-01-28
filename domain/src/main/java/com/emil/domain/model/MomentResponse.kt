package com.emil.domain.model

data class MomentResponse (val momentId:Long,val authorId:Long,val authorUsername:String,val authorAvatarUrl:String,
                      val imageUrl:String?,val videoUrl:String?,val audioUrl:String?,
                      val text:String?,val publishDate:String,val confirmed:Boolean)