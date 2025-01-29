package com.emil.data.model

import com.emil.domain.model.ChatResponse

data class ChatResponseDto  (val chatId: Long=0, val companionId:Long?=null,val senderId:Long? = null, val isGroup:Boolean=false,val avatarUrl: String="",
                        val displayName: String="",val confirmed:Boolean = false, val lastMessage: String="",val dateLast:String="",
                        val unreadMessagesCount:Long?=null)




fun ChatResponseDto.toDomainModel():ChatResponse{
   return ChatResponse(chatId,companionId,senderId,isGroup,avatarUrl, displayName,confirmed, lastMessage, dateLast,unreadMessagesCount)
}
fun List<ChatResponseDto>.toDomainModelList(): List<ChatResponse> {
    return this.map { it.toDomainModel() }
}