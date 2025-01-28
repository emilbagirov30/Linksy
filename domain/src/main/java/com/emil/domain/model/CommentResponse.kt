package com.emil.domain.model

data class CommentResponse (val commentId:Long,val authorId:Long,val  authorAvatarUrl:String, val authorName:String,val confirmed:Boolean,
    val parentCommentId:Long?,val commentText:String, val date:String)