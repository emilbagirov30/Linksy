package com.emil.domain.model

data class MessageResponse(
    val messageId: Long,
    val senderId: Long,
    val chatId: Long,
    val text: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val audioUrl: String?,
    val voiceUrl: String?,
    val date:String,
    var viewed:Boolean
)


fun MessageResponse.toLocalModel():MessageLocal{
    return MessageLocal(messageId,senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date,viewed)
}