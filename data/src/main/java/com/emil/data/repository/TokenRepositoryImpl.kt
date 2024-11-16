package com.emil.data.repository

import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.Token
import com.emil.domain.repository.TokenRepository
import retrofit2.Response

class TokenRepositoryImpl:TokenRepository {
    override suspend fun refreshToken(token: String): Response<Token> {
        val response = RetrofitInstance.apiService.refreshToken(token)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}