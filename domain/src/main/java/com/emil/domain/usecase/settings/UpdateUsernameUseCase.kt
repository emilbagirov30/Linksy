package com.emil.domain.usecase.settings

import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UpdateUsernameUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String,username:String):Response<Unit>{
        return userRepository.updateUsername(token,username)
    }
}