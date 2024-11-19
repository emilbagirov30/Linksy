package com.emil.domain.usecase

import com.emil.domain.model.AllUserData
import com.emil.domain.model.UserProfileData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class AllUserDataUseCase(private val userRepository: UserRepository) {
    suspend fun execute (token:String): Response<AllUserData> {
        return userRepository.getAllUserData(token)
    }

}