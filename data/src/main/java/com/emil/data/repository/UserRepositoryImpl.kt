package com.emil.data.repository

import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.UserProfileData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    override suspend fun getUserData(token: String): Response<UserProfileData> {
        val response = RetrofitInstance.apiService.getUserData("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}