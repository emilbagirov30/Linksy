package com.emil.domain.usecase

import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class ConfirmCodeUseCase (private val authRepository:AuthRepository) {
    suspend fun execute (param:ConfirmCodeParam): Response<Unit> {
        return authRepository.confirmCode(param)
    }
}