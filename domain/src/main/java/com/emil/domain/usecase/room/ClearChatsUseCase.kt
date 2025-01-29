package com.emil.domain.usecase.room

import com.emil.domain.repository.ChatRepository

class ClearChatsUseCase (private val chatRepository: ChatRepository){
    suspend fun execute(){
        return chatRepository.clearChats()
    }
}