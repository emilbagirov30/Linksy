package com.emil.domain.repository

import com.emil.domain.model.UserResponse
import retrofit2.Response

interface PeopleRepository {
    suspend fun findByUsername (token:String, startsWith:String):Response<List<UserResponse>>
    suspend fun findByLink (token:String, startsWith:String):Response<List<UserResponse>>
}