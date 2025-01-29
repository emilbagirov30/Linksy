package com.emil.domain.usecase.chat

import com.emil.domain.repository.ChatRepository

class CheckIsGroupUseCase (private val chatRepository: ChatRepository) {
    suspend fun execute (chatId:Long):Boolean{
        return chatRepository.isGroup(chatId)
    }
}