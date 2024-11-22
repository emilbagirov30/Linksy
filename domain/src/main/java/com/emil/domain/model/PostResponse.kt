package com.emil.domain.model

data class PostResponse (val postId:Long,val authorUsername:String,val authorAvatarUrl:String,
                         val text:String,val publishDate:String,
                         val likesCount:Int,val repostsCount:Int)


