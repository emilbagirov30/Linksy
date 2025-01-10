package com.emil.data.model

import com.emil.domain.model.ChatResponse

class ChatResponseDto  (val chatId: Long=0, val companionId:Long?=null, val isGroup:Boolean=false,val avatarUrl: String="", val displayName: String="", val lastMessage: String?=null,val dateLast:String="")




fun ChatResponseDto.toDomainModel():ChatResponse{
   return ChatResponse(chatId,companionId,isGroup,avatarUrl, displayName, lastMessage, dateLast)
}
fun List<ChatResponseDto>.toDomainModelList(): List<ChatResponse> {
    return this.map { it.toDomainModel() }
}