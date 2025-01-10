package com.emil.domain.usecase

import com.emil.domain.model.ChatLocal
import com.emil.domain.repository.ChatRepository

class InsertChatInLocalDbUseCase (private val chatRepository: ChatRepository) {
    suspend fun execute(chatLocal: ChatLocal){
        chatRepository.insertChat(chatLocal)
    }
}