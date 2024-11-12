package com.emil.domain.usecase

import com.emil.domain.model.UserProfileData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UserDataUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String):Response<UserProfileData>{
        return userRepository.getUserData(token)
    }
}