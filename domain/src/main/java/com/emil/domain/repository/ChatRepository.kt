package com.emil.domain.repository

import com.emil.domain.model.ChatLocal
import com.emil.domain.model.ChatResponse
import retrofit2.Response

interface ChatRepository {
    suspend fun getUserChats (token:String):Response<List<ChatResponse>>
    suspend fun getAllChats(): List<ChatLocal>
    suspend fun getChatId(token: String,userId:Long): Response<Long>
    suspend fun insertChat(chat: ChatLocal)
    suspend fun isGroup(chatId: Long):Boolean
}