package com.emil.data.repository

import com.emil.data.model.ChatLocalDb
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.ChatLocal
import com.emil.domain.model.ChatResponse
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class ChatRepositoryImpl(private val chatDao: ChatDao):ChatRepository {
    private val chatLocalDb = ChatLocalDb();
    override suspend fun getUserChats(token: String): Response<List<ChatResponse>> {
        val response = RetrofitUserInstance.apiService.getUserChats("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getAllChats(): List<ChatLocal> {
      return chatDao.getAllChats().toDomainModelList()
    }

    override suspend fun getChatId(token: String, userId: Long): Response<Long> {
        return RetrofitUserInstance.apiService.getChatId("Bearer $token",userId)
    }

    override suspend fun insertChat(chat: ChatLocal) {
        return chatDao.insertChat(chatLocalDb.toDomainModel(chat))
    }

    override suspend fun isGroup(chatId:Long):Boolean {
        return chatDao.isGroup(chatId)
    }
}
