package com.emil.data.repository

import com.emil.data.model.MessageBody
import com.emil.data.model.MomentBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitCloudInstance
import com.emil.domain.model.MessageData
import com.emil.domain.repository.MessageRepository
import retrofit2.Response

class MessageRepositoryImpl:MessageRepository {
    private val messageBody = MessageBody ()
    override suspend fun sendMessage(token: String, message: MessageData): Response<Unit> {
        return RetrofitCloudInstance.apiService.sendMessage("Bearer $token",
            messageBody.toDomainModel(message).recipientId,
            messageBody.toDomainModel(message).text,
            messageBody.toDomainModel(message).image,
            messageBody.toDomainModel(message).video,
            messageBody.toDomainModel(message).audio,
            messageBody.toDomainModel(message).voice
        )
    }
}