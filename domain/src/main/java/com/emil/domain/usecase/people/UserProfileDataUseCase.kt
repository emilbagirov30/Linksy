package com.emil.domain.usecase.people

import com.emil.domain.model.UserProfileData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UserProfileDataUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String):Response<UserProfileData>{
        return userRepository.getUserProfileData(token)
    }
}