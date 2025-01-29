package com.emil.domain.usecase.settings

import com.emil.domain.model.AllUserData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class GetAllUserDataUseCase(private val userRepository: UserRepository) {
    suspend fun execute (token:String): Response<AllUserData> {
        return userRepository.getAllUserData(token)
    }

}