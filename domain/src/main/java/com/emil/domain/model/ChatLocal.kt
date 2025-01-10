package com.emil.domain.model

data class ChatLocal(
    val id: Long,
    val companionId:Long?,
    val isGroup: Boolean,
    val avatarUrl: String,
    val displayName: String,
    val lastMessage: String?,
    val dateLast: String
)


fun ChatLocal.toResponseModel():ChatResponse{
   return ChatResponse(id,companionId,isGroup, avatarUrl, displayName, lastMessage, dateLast)
}

fun List<ChatLocal>.toResponseModelList():List<ChatResponse>{
    return this.map { it.toResponseModel() }
}