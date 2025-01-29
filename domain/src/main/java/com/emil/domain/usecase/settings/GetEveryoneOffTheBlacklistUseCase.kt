package com.emil.domain.usecase.settings

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class GetEveryoneOffTheBlacklistUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String): Response<List<UserResponse>> {
        return userRepository.getEveryoneOffTheBlacklist(token)
    }}