package com.emil.domain.usecase

import com.emil.domain.repository.UserRepository
import okhttp3.MultipartBody
import retrofit2.Response

class UploadAvatarUseCase (private val userRepository: UserRepository) {
    suspend fun execute (token:String,avatar: MultipartBody.Part): Response<Unit>{
        return userRepository.uploadAvatar(token,avatar)
    }
}