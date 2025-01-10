package com.emil.domain.repository

import com.emil.domain.model.MessageData
import com.emil.domain.model.MessageLocal
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.PostData
import retrofit2.Response

interface MessageRepository {
    suspend fun sendMessage (token:String,message: MessageData): Response<Unit>
    suspend fun getUserMessages (token:String):Response<List<MessageResponse>>


    suspend fun getMessagesByChat(chatId: Long): List<MessageLocal>
    suspend fun insertMessage(message:MessageLocal)
}