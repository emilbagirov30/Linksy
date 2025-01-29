package com.emil.domain.usecase.settings

import com.emil.domain.model.MessageMode
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class SetMessageModeUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token: String,messageMode: MessageMode): Response<Unit> {
        return userRepository.setMessageMode(token,messageMode);
    }}