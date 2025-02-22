package com.emil.data.model

import com.emil.domain.model.PostResponse


data class PostResponseDto (val postId:Long=0,
                            val authorId:Long = 0,
                            val authorUsername:String="",
                            val confirmed:Boolean = false,
                            val authorAvatarUrl:String="",
                            val imageUrl:String?="",
                            val videoUrl:String?="",
                            val audioUrl:String?="",
                            val voiceUrl:String?="",
                            val text:String?=null,
                            val publishDate:String="",
                            val likesCount:Long=0,
                            val commentsCount:Long = 0,
                            val isLikedIt:Boolean,
                            val edited:Boolean)


fun PostResponseDto.toDomainModel():PostResponse {
   return PostResponse(postId = postId,authorId = authorId,authorUsername =authorUsername,
       confirmed = confirmed,authorAvatarUrl=authorAvatarUrl,imageUrl = imageUrl,videoUrl =videoUrl,
       audioUrl = audioUrl,voiceUrl = voiceUrl, text=text,publishDate=publishDate,
       likesCount=likesCount, commentsCount = commentsCount, isLikedIt = isLikedIt, edited = edited)
}
fun List<PostResponseDto>.toDomainModelList(): List<PostResponse> {
    return this.map { it.toDomainModel() }
}