package com.emil.domain.repository

import com.emil.domain.model.MessageData
import com.emil.domain.model.PostData
import retrofit2.Response

interface MessageRepository {
    suspend fun sendMessage (token:String,message: MessageData): Response<Unit>
}