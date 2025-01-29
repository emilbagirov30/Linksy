package com.emil.domain.usecase.auth

import com.emil.domain.model.PasswordRecoveryData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class ConfirmPasswordRecoveryUseCase(private val authRepository: AuthRepository) {

    suspend fun execute (passwordRecoveryData: PasswordRecoveryData): Response<Unit> {
        return authRepository.confirmPasswordChange(passwordRecoveryData)
    }
}