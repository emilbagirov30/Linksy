package com.emil.domain.usecase

import com.emil.domain.model.Token
import com.emil.domain.model.UserLoginData
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend fun execute (userData: UserLoginData): Response<Token> {
        return authRepository.logIn(userData)
    }
}