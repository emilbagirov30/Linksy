package com.emil.domain.usecase

import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UpdateBirthdayUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String,birthday:String): Response<Unit> {
        return userRepository.updateBirthday(token,birthday)
    }
}