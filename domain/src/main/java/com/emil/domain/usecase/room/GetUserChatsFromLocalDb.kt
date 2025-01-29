package com.emil.domain.usecase.room

import com.emil.domain.model.ChatLocal
import com.emil.domain.repository.ChatRepository

class GetUserChatsFromLocalDb (private val chatRepository: ChatRepository) {
    suspend fun execute ():List<ChatLocal>{
       return chatRepository.getAllChats()
    }
}