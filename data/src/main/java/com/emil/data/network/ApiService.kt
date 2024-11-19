package com.emil.data.network

import com.emil.data.model.AllUserDataDto
import com.emil.data.model.PasswordChangeBody
import com.emil.data.model.RegistrationBody
import com.emil.data.model.TokenDto
import com.emil.data.model.UserLoginBody
import com.emil.data.model.UserProfileDataDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body request: RegistrationBody): Response<Unit>

    @POST("api/users/confirm")
    suspend fun confirmCode(@Query("email") email:String, @Query("code") code: String): Response<Unit>
    @POST("api/users/resend_code")
    suspend fun resendCode(@Query("email") emailParam:String): Response<Unit>
    @POST("api/users/login")
    suspend fun login(@Body request:UserLoginBody): Response<TokenDto>
    @POST("api/users/request_password_change")
    suspend fun requestPasswordChange(@Query("email") emailParam:String): Response<Unit>

    @POST("api/users/confirm_password_change")
    suspend fun confirmPasswordChange(@Body request:PasswordChangeBody): Response<Unit>
    @GET ("api/users/profile_data")
    suspend fun getUserProfileData (@Header("Authorization") token:String): Response<UserProfileDataDto>
    @GET ("api/users/all_data")
    suspend fun getAllUserData (@Header("Authorization") token:String): Response<AllUserDataDto>
    @Multipart
    @POST ("/api/users/upload/avatar")
    suspend fun uploadAvatar (@Header("Authorization") token:String, @Part file: MultipartBody.Part): Response<Unit>
    @POST("api/users/refresh_token")
    suspend fun refreshToken (@Query("refreshToken") token:String):Response<TokenDto>
    @POST("api/users/update_birthday")
    suspend fun updateBirthday (@Header("Authorization") token:String,@Query("birthday") birthday:String):Response<Unit>
}
