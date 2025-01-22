package com.emil.domain.model

data class MessageResponse(
    val messageId: Long,
    val senderId: Long,
    val chatId: Long,
    var text: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val audioUrl: String?,
    val voiceUrl: String?,
    val date:String,
    var viewed:Boolean,
    var edited:Boolean
)


fun MessageResponse.toLocalModel():MessageLocal{
    return MessageLocal(messageId,senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date,viewed,edited)
}