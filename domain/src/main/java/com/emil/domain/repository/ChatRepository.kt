package com.emil.domain.repository

import com.emil.domain.model.ChatLocal
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.UserResponse
import retrofit2.Response

interface ChatRepository {
    suspend fun getUserChats (token:String):Response<List<ChatResponse>>
    suspend fun getAllChats(): List<ChatLocal>
    suspend fun getChatId(token: String,userId:Long): Response<Long>
    suspend fun insertChat(chat: ChatLocal)
    suspend fun isGroup(chatId: Long):Boolean
    suspend fun createGroup(token:String,groupData: GroupData):Response<Unit>
    suspend fun getGroupMembers(token:String,groupId: Long):Response<List<UserResponse>>
}