package com.emil.data.model

import com.emil.domain.model.MessageResponse


data class MessageResponseDto(val messageId:Long=0,val senderId: Long=0,val chatId: Long=0, val text: String?=null,
    val imageUrl: String?=null, val videoUrl: String?=null, val audioUrl: String?=null,val voiceUrl: String?=null, val date:String=""
)

fun MessageResponseDto.toDomainModel():MessageResponse{
   return MessageResponse(messageId,senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date)
}
fun MutableList<MessageResponseDto>.toDomainModelList(): MutableList<MessageResponse> {
    return this.map { it.toDomainModel() }.toMutableList()
}