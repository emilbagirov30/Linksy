package com.emil.domain.repository

import com.emil.domain.model.MessageData
import com.emil.domain.model.MessageLocal
import com.emil.domain.model.MessageResponse
import retrofit2.Response

interface MessageRepository {
    suspend fun sendMessage (token:String,message: MessageData): Response<Unit>
    suspend fun getUserMessages (token:String):Response<MutableList<MessageResponse>>
    suspend fun getMessagesByChatFromLocalDb(chatId: Long): MutableList<MessageLocal>
    suspend fun insertMessage(message:MessageLocal)
    suspend fun getMessagesByChat (token:String,chatId:Long):Response<MutableList<MessageResponse>>
    suspend fun viewed (token:String,chatId:Long):Response<Unit>
    suspend fun deleteMessage (token:String,messageId:Long):Response<Unit>
}