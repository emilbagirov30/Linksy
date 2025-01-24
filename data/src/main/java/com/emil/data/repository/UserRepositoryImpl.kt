package com.emil.data.repository

import com.emil.data.model.PasswordChangeBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.AllUserData
import com.emil.domain.model.MessageMode
import com.emil.domain.model.PasswordChangeData
import com.emil.domain.model.Token
import com.emil.domain.model.UserProfileData
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.UserRepository
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    private val passwordChangeRequest = PasswordChangeBody ()
    override suspend fun getUserProfileData(token: String): Response<UserProfileData> {
        val response = RetrofitUserInstance.apiService.getUserProfileData("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getAllUserData(token: String): Response<AllUserData> {
        val response = RetrofitUserInstance.apiService.getAllUserData("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun uploadAvatar(token: String,avatar: MultipartBody.Part): Response<Unit> {
        return RetrofitCloudInstance.apiService.uploadAvatar("Bearer $token",avatar)
    }
    override suspend fun updateBirthday(token: String, birthday: String): Response<Unit> {
        return RetrofitUserInstance.apiService.updateBirthday("Bearer $token",birthday)
    }
    override suspend fun updateUsername(token: String, username: String): Response<Unit> {
        return RetrofitUserInstance.apiService.updateUsername("Bearer $token",username)
    }
    override suspend fun updateLink(token: String, link: String): Response<Unit> {
        return RetrofitUserInstance.apiService.updateLink("Bearer $token",link)
    }
    override suspend fun deleteAvatar(token: String): Response<Unit> {
        return RetrofitUserInstance.apiService.deleteAvatar("Bearer $token")
    }

    override suspend fun changePassword(
        token: String,
        passwordChangeData: PasswordChangeData
    ): Response<Token> {
       val response = RetrofitUserInstance.apiService.changePassword("Bearer $token",passwordChangeRequest.toDomainModel(passwordChangeData))
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getEveryoneOffTheBlacklist(token: String): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getBlackList("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getMessageMode(token: String): Response<MessageMode> {
        return RetrofitUserInstance.apiService.getMessageMode("Bearer $token")
    }

    override suspend fun setMessageMode(token: String, messageMode: MessageMode): Response<Unit> {
        return  RetrofitUserInstance.apiService.setMessageMode("Bearer $token",messageMode)
    }
}