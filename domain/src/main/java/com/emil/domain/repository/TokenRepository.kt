package com.emil.domain.repository

import com.emil.domain.model.Token
import retrofit2.Response

interface  TokenRepository {
    suspend fun refreshToken (token:String): Response<Token>
}