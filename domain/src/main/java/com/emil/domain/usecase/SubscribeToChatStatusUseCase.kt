package com.emil.domain.usecase

import com.emil.domain.model.Status
import com.emil.domain.model.StatusResponse
import com.emil.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class SubscribeToChatStatusUseCase (private val repository: WebSocketRepository){
     fun invoke (token:String,chatId:Long): Flow<StatusResponse> {
        return repository.subscribeToStatusMessages(token,chatId)
    }
}