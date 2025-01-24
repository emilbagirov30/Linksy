package com.emil.domain.model

data class PostResponse (val postId:Long, val authorId:Long,val authorUsername:String, val authorAvatarUrl:String,
                         val imageUrl:String?, val videoUrl:String?, val audioUrl:String?, val voiceUrl:String?,
                         val text:String?, val publishDate:String,
                         var likesCount:Long, val commentsCount:Long,
                         var isLikedIt:Boolean,val edited:Boolean)


