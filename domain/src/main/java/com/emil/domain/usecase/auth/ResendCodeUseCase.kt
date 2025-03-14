package com.emil.domain.usecase.auth

import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class ResendCodeUseCase (private val authRepository: AuthRepository) {
    suspend fun execute (emailParam:String): Response<Unit> {
        return authRepository.resendCode(emailParam)
    }
}