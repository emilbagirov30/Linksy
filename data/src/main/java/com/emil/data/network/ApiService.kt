package com.emil.data.network

import com.emil.data.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body request: RegistrationRequest): Response<Unit>

}
