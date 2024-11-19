package com.emil.domain.usecase

import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class RequestPasswordRecoveryUseCase(private val authRepository: AuthRepository) {
    suspend fun execute (emailParam:String): Response<Unit> {
        return authRepository.requestPasswordChange(emailParam)
    }
}