package com.emil.domain.usecase

import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class RegisterUseCase (private val userRepository: UserRepository) {
    suspend fun execute (userData:UserRegistrationData):Response<Unit>{
         return userRepository.registerUser(userData)
    }
}