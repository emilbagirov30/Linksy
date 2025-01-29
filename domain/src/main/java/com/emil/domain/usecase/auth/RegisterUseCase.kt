package com.emil.domain.usecase.auth

import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class RegisterUseCase (private val authRepository: AuthRepository) {
    suspend fun execute (userData:UserRegistrationData):Response<Unit>{
         return authRepository.registerUser(userData)
    }
}