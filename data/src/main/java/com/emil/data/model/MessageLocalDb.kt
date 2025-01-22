package com.emil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emil.domain.model.MessageLocal
import com.emil.domain.model.MomentResponse

@Entity(tableName = "messages")
data class MessageLocalDb(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val senderId: Long=0,
    val chatId: Long=0,
    val text: String?="",
    val imageUrl: String?="",
    val videoUrl: String?="",
    val audioUrl: String?="",
    val voiceUrl: String?="",
    val date: String="",
    val viewed:Boolean = false,
    val edited:Boolean = false
)



fun MessageLocalDb.toDomainModel ():MessageLocal{
  return MessageLocal(id, senderId, chatId, text, imageUrl, videoUrl, audioUrl, voiceUrl, date,viewed,edited)
}

fun MessageLocalDb.toDomainModel (domainModel: MessageLocal):MessageLocalDb{
    return MessageLocalDb(domainModel.id, domainModel.senderId, domainModel.chatId, domainModel.text, domainModel.imageUrl,
        domainModel.videoUrl, domainModel.audioUrl, domainModel.voiceUrl, domainModel.date,domainModel.viewed,domainModel.edited)
}

fun MutableList<MessageLocalDb>.toDomainModelList(): MutableList<MessageLocal> {
    return this.map { it.toDomainModel() }.toMutableList()
}