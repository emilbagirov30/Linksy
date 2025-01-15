package com.emil.data.model

import com.emil.domain.model.CommentResponse

class CommentResponseDto(val commentId:Long =0,val authorId:Long = 0,val  authorAvatarUrl:String="", val authorName:String="",
                         val parentCommentId:Long?,val commentText:String="", val date:String="")

  fun CommentResponseDto.toDomainModel ():CommentResponse {
     return CommentResponse (commentId,authorId, authorAvatarUrl, authorName, parentCommentId, commentText, date)
  }

   fun List<CommentResponseDto>.toDomainModelList ():List<CommentResponse>{
       return this.map { it.toDomainModel() }
   }