package com.emil.domain.usecase

import com.emil.domain.model.Token
import com.emil.domain.repository.TokenRepository
import retrofit2.Response

class RefreshTokenUseCase(private val tokenRepository: TokenRepository) {
    suspend fun execute (token:String): Response<Token>{
       return tokenRepository.refreshToken(token)
    }
}