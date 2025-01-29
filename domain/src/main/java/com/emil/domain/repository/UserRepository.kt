package com.emil.domain.repository

import com.emil.domain.model.AllUserData
import com.emil.domain.model.MessageMode
import com.emil.domain.model.PasswordChangeData
import com.emil.domain.model.Token
import com.emil.domain.model.UserProfileData
import com.emil.domain.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface UserRepository {
    suspend fun getUserProfileData (token:String):Response<UserProfileData>
    suspend fun getAllUserData (token:String):Response<AllUserData>
    suspend fun uploadAvatar (token:String,avatar: MultipartBody.Part):Response<Unit>
    suspend fun updateBirthday (token:String,birthday:String):Response<Unit>
    suspend fun updateUsername (token:String,username:String):Response<Unit>
    suspend fun updateLink (token:String,link:String):Response<Unit>
    suspend fun deleteAvatar(token:String):Response<Unit>
    suspend fun changePassword(token:String,passwordChangeData: PasswordChangeData):Response<Token>
    suspend fun getEveryoneOffTheBlacklist (token:String):Response<List<UserResponse>>
    suspend fun getMessageMode (token:String):Response<MessageMode>
    suspend fun setMessageMode (token:String,messageMode:MessageMode):Response<Unit>
}