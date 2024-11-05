package com.emil.domain.usecase

import com.emil.domain.model.PasswordChangeData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class ConfirmPasswordChangeUseCase(private val authRepository: AuthRepository) {

    suspend fun execute (passwordChangeData: PasswordChangeData): Response<Unit> {
        return authRepository.confirmPasswordChange(passwordChangeData)
    }
}