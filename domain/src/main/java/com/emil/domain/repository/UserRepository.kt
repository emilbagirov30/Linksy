package com.emil.domain.repository

import com.emil.domain.model.AllUserData
import com.emil.domain.model.UserProfileData
import retrofit2.Response

interface UserRepository {
    suspend fun getUserProfileData (token:String):Response<UserProfileData>
    suspend fun getAllUserData (token:String):Response<AllUserData>
}