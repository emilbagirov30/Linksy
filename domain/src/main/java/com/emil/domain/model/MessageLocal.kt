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
    val date: String
)

fun MessageLocal.toResponseModel():MessageResponse{
    return MessageResponse(id,senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date)
}

fun List<MessageLocal>.toResponseModelList():MutableList<MessageResponse>{
    return this.map { it.toResponseModel() }.toMutableList()
}