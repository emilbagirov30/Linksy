package com.emil.data.repository

import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.AllUserData
import com.emil.domain.model.UserProfileData
import com.emil.domain.repository.UserRepository
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    override suspend fun getUserProfileData(token: String): Response<UserProfileData> {
        val response = RetrofitInstance.apiService.getUserProfileData("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getAllUserData(token: String): Response<AllUserData> {
        val response = RetrofitInstance.apiService.getAllUserData("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun uploadAvatar(token: String,file: MultipartBody.Part): Response<Unit> {
        return RetrofitInstance.apiService.uploadAvatar("Bearer $token",file)
    }

    override suspend fun updateBirthday(token: String, birthday: String): Response<Unit> {
        return RetrofitInstance.apiService.updateBirthday("Bearer $token",birthday)
    }
}