package com.emil.data.model

import com.emil.domain.model.PostResponse


data class PostResponseDto (val postId:Long=0,val authorUsername:String="",val authorAvatarUrl:String="",val imageUrl:String?="",
                            val videoUrl:String?="",val audioUrl:String?="",val voiceUrl:String?="",
                            val text:String?=null,val publishDate:String="",val likesCount:Int=0,val repostsCount:Int=0)


fun PostResponseDto.toDomainModel():PostResponse {
   return PostResponse(postId = postId,authorUsername =authorUsername,authorAvatarUrl=authorAvatarUrl,imageUrl = imageUrl,videoUrl =videoUrl,
       audioUrl = audioUrl,voiceUrl = voiceUrl, text=text,publishDate=publishDate, likesCount=likesCount,repostsCount=repostsCount)
}
fun List<PostResponseDto>.toDomainModelList(): List<PostResponse> {
    return this.map { it.toDomainModel() }
}