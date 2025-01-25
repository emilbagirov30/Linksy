package com.emil.domain.repository

import com.emil.domain.model.ChatLocal
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.GroupEditData
import com.emil.domain.model.GroupResponse
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
    suspend fun getGroupData(token:String,groupId: Long):Response<GroupResponse>
    suspend fun editGroup(token:String, editData: GroupEditData):Response<Unit>
    suspend fun deleteChat(token:String, chatId:Long):Response<Unit>
    suspend fun leaveTheGroup(token:String, groupId:Long):Response<Unit>
    suspend fun addMembers(token:String,groupId: Long, newMembers:List<Long>):Response<Unit>
    suspend fun clearChats ()
}