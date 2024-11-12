package com.emil.domain.repository

import com.emil.domain.model.UserProfileData
import retrofit2.Response

interface UserRepository {
    suspend fun getUserData (token:String):Response<UserProfileData>
}