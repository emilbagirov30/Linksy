package com.emil.data.network

import com.emil.data.model.RegistrationBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body request: RegistrationBody): Response<Unit>

    @POST("api/users/confirm")
    suspend fun confirmCode(@Query("email") email:String, @Query("code") code: String): Response<Unit>
    @POST("api/users/resend_code")
    suspend fun resendCode(@Query("email") emailParam:String): Response<Unit>
}
