package com.emil.domain.usecase.chat

import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class DeleteChatUseCase  (private val chatRepository: ChatRepository){
    suspend fun execute (token:String,chatId:Long): Response<Unit>{
        return chatRepository.deleteChat(token, chatId)
    }
}