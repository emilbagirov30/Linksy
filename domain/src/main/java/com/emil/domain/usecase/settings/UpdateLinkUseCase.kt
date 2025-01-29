package com.emil.domain.usecase.settings

import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UpdateLinkUseCase(private val userRepository: UserRepository) {
    suspend fun execute (token:String,link:String):Response<Unit>{
        return userRepository.updateLink(token,link)
    }
}