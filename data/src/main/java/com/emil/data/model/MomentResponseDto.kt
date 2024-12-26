package com.emil.data.model

import com.emil.domain.model.MomentResponse
import com.emil.domain.model.PostResponse

data class MomentResponseDto(val momentId:Long=0,val authorUsername:String="",val authorAvatarUrl:String="",
                             val imageUrl:String?=null,val videoUrl:String?=null,val audioUrl:String?=null,
                             val text:String?=null,val publishDate:String="")



fun MomentResponseDto.toDomainModel(): MomentResponse {
    return MomentResponse(momentId = momentId,authorUsername =authorUsername,authorAvatarUrl=authorAvatarUrl,imageUrl = imageUrl,videoUrl =videoUrl,
        audioUrl = audioUrl, text=text,publishDate=publishDate)
}
fun List<MomentResponseDto>.toDomainModelList(): List<MomentResponse> {
    return this.map { it.toDomainModel() }
}