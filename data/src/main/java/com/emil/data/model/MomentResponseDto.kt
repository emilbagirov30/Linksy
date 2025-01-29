package com.emil.data.model

import com.emil.domain.model.MomentResponse


data class MomentResponseDto(val momentId:Long=0,val authorId:Long = 0,val authorUsername:String="",val authorAvatarUrl:String="",
                             val imageUrl:String?=null,val videoUrl:String?=null,val audioUrl:String?=null,
                             val text:String?=null,val publishDate:String="",val confirmed:Boolean = false)



fun MomentResponseDto.toDomainModel(): MomentResponse {
    return MomentResponse(momentId = momentId,authorId = authorId,authorUsername =authorUsername,authorAvatarUrl=authorAvatarUrl,imageUrl = imageUrl,videoUrl =videoUrl,
        audioUrl = audioUrl, text=text,publishDate=publishDate,confirmed = confirmed)
}
fun List<MomentResponseDto>.toDomainModelList(): List<MomentResponse> {
    return this.map { it.toDomainModel() }
}