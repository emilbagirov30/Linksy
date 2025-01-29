package com.emil.data.model

import com.emil.domain.model.CommentData

data class CommentBody (val postId:Long = 0,val text:String = "", val parentCommentId:Long?=null)



fun CommentBody.toDomainModel (domainModel: CommentData):CommentBody{
    return CommentBody(postId = domainModel.postId,text = domainModel.text,parentCommentId = domainModel.parentCommentId)
}