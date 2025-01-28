package com.emil.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emil.domain.model.ChatLocal
import com.emil.domain.model.MessageLocal

@Entity(tableName = "chats")
data class ChatLocalDb(
    @PrimaryKey(autoGenerate = false) val id: Long=0,
    val companionId:Long? = null,
    val senderId:Long? = null,
    val isGroup: Boolean=false,
    val avatarUrl: String="",
    val displayName: String="",
    val confirmed:Boolean = false,
    val lastMessage: String?="",
    val dateLast: String="",
    val unreadMessagesCount:Long? = null
)

fun ChatLocalDb.toDomainModel (): ChatLocal {
    return ChatLocal(id,companionId, senderId,isGroup, avatarUrl, displayName, confirmed,lastMessage, dateLast,unreadMessagesCount)
}
fun ChatLocalDb.toDomainModel (domainModel: ChatLocal): ChatLocalDb {
    return ChatLocalDb(domainModel.id,domainModel.companionId, domainModel.senderId,domainModel.isGroup, domainModel.avatarUrl, domainModel.displayName, domainModel.confirmed,domainModel.lastMessage, domainModel.dateLast)
}

fun List<ChatLocalDb>.toDomainModelList(): List<ChatLocal> {
    return this.map { it.toDomainModel() }
}