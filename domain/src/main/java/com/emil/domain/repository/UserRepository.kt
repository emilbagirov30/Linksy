package com.emil.domain.repository

import com.emil.domain.model.AllUserData
import com.emil.domain.model.UserProfileData
import okhttp3.MultipartBody
import retrofit2.Response

interface UserRepository {
    suspend fun getUserProfileData (token:String):Response<UserProfileData>
    suspend fun getAllUserData (token:String):Response<AllUserData>
    suspend fun uploadAvatar (token:String,file: MultipartBody.Part):Response<Unit>
    suspend fun updateBirthday (token:String,birthday:String):Response<Unit>

}