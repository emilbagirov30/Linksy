package com.emil.domain.usecase

import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class ConfirmCodeUseCase (private val userRepository:UserRepository) {
    suspend fun execute (param:ConfirmCodeParam): Response<Unit> {
        return userRepository.confirmCode(param)
    }
}