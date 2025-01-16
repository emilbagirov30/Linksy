package com.emil.domain.usecase

import com.emil.domain.model.MessageMode
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class GetMessageModeUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token: String): Response<MessageMode>{
        return userRepository.getMessageMode(token)
}}