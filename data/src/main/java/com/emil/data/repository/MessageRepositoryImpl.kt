package com.emil.data.repository

import com.emil.data.model.MessageBody
import com.emil.data.model.MessageLocalDb
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.MessageData
import com.emil.domain.model.MessageLocal
import com.emil.domain.model.MessageResponse
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class MessageRepositoryImpl(private val messageDao: MessageDao):MessageRepository {
    private val messageBody = MessageBody ()
    private val messageLocalDb = MessageLocalDb()
    override suspend fun sendMessage(token: String, message: MessageData): Response<Unit> {
        return RetrofitInstance.apiService.sendMessage("Bearer $token",
            messageBody.toDomainModel(message).recipientId,
            messageBody.toDomainModel(message).chatId,
            messageBody.toDomainModel(message).text,
            messageBody.toDomainModel(message).image,
            messageBody.toDomainModel(message).video,
            messageBody.toDomainModel(message).audio,
            messageBody.toDomainModel(message).voice
        )
    }

    override suspend fun getUserMessages(token: String): Response<MutableList<MessageResponse>> {
        val response = RetrofitInstance.apiService.getUserMessages("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getMessagesByChatFromLocalDb(chatId: Long): MutableList<MessageLocal> {
       return messageDao.getMessagesByChat(chatId).toDomainModelList()
    }

    override suspend fun insertMessage(message: MessageLocal) {
       return messageDao.insertMessage(messageLocalDb.toDomainModel(message))
    }

    override suspend fun getMessagesByChat(token: String, chatId: Long): Response<MutableList<MessageResponse>> {
        val response = RetrofitInstance.apiService.getUserMessagesByChat("Bearer $token",chatId)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun viewed(token: String, chatId: Long): Response<Unit> {
        return RetrofitInstance.apiService.viewed("Bearer $token",chatId)

    }

    override suspend fun deleteMessage(token: String, messageId: Long): Response<Unit> {
        return RetrofitInstance.apiService.deleteMessage("Bearer $token",messageId)
    }

    override suspend fun deleteMessageFromLocalDb(messageId: Long) {
        return messageDao.deleteMessageById(messageId)
    }

    override suspend fun clearAllMessages() {
        return messageDao.clearAllMessages()
    }

    override suspend fun editMessage(token: String, messageId: Long, text: String): Response<Unit> {
        return RetrofitInstance.apiService.editMessage("Bearer $token",messageId,text)
    }


}