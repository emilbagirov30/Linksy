package com.emil.domain.usecase

import com.emil.domain.repository.UserRepository
import retrofit2.Response

class DeleteAvatarUseCase(private val userRepository: UserRepository){
    suspend fun execute(token:String): Response<Unit> {
        return userRepository.deleteAvatar(token)
    }
}