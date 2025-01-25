package com.emil.data.model

import com.emil.domain.model.PostAppreciatedResponse

class PostAppreciatedResponseDto (val id:Long=0,val avatarUrl:String="",val username:String="",
                                  val link:String?=null,val online:Boolean=false,val confirmed:Boolean=false,val score:Int=0)







fun PostAppreciatedResponseDto.toDomainModel ():PostAppreciatedResponse {
    return  PostAppreciatedResponse(id, avatarUrl, username, link, online, confirmed, score)
}



fun List<PostAppreciatedResponseDto>.toDomainModelList ():List<PostAppreciatedResponse>{
    return this.map { it.toDomainModel() }
}