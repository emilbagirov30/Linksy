package com.emil.domain.model

data class ChatLocal(
    val id: Long,
    val companionId:Long?,
    val senderId:Long?,
    val isGroup: Boolean,
    val avatarUrl: String,
    val displayName: String,
    val confirmed:Boolean,
    val lastMessage: String?,
    val dateLast: String,    val unreadMessagesCount:Long?
)


fun ChatLocal.toResponseModel():ChatResponse{
   return ChatResponse(id,companionId,senderId,isGroup, avatarUrl, displayName,confirmed, lastMessage, dateLast,unreadMessagesCount)
}

fun List<ChatLocal>.toResponseModelList():List<ChatResponse>{
    return this.map { it.toResponseModel() }
}