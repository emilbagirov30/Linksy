package com.emil.domain.model


data class ChatResponse (val chatId: Long,val companionId:Long?,val isGroup:Boolean, val avatarUrl: String, val displayName: String, val lastMessage: String?,val dateLast:String)

fun ChatResponse.toLocalModel():ChatLocal{
    return ChatLocal(chatId,companionId,isGroup, avatarUrl, displayName, lastMessage, dateLast)
}
