package com.emil.domain.model

data class MessageLocal(
    val id: Long = 0,
    val senderId: Long,
    val chatId: Long,
    val text: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val audioUrl: String?,
    val voiceUrl: String?,
    val date: String,
    val viewed:Boolean,
    val edited:Boolean
)

fun MessageLocal.toResponseModel():MessageResponse{
    return MessageResponse(id,senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date,viewed,edited)
}

fun List<MessageLocal>.toResponseModelList():MutableList<MessageResponse>{
    return this.map { it.toResponseModel() }.toMutableList()
}