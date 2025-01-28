package com.emil.domain.model


data class ChatResponse (val chatId: Long,val companionId:Long?,val senderId:Long?,val isGroup:Boolean,
                         val avatarUrl: String, val displayName: String,val confirmed:Boolean,val lastMessage: String?,val dateLast:String,
                         val unreadMessagesCount:Long?)

fun ChatResponse.toLocalModel():ChatLocal{
    return ChatLocal(chatId,companionId,senderId,isGroup, avatarUrl, displayName,confirmed, lastMessage, dateLast,unreadMessagesCount)
}
