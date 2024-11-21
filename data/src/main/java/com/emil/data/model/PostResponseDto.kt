package com.emil.data.model

import com.emil.domain.model.PostResponse


data class PostResponseDto (val authorUsername:String="",val authorAvatarUrl:String="",
                         val text:String="",val publishDate:String="",
                         val likesCount:Int=0,val repostsCount:Int=0)


fun PostResponseDto.toDomainModel():PostResponse {
   return PostResponse(authorUsername =authorUsername,authorAvatarUrl=authorAvatarUrl,
       text=text,publishDate=publishDate, likesCount=likesCount,repostsCount=repostsCount)
}
fun List<PostResponseDto>.toDomainModelList(): List<PostResponse> {
    return this.map { it.toDomainModel() }
}