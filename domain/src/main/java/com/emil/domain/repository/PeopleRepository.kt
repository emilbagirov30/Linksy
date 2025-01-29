package com.emil.domain.repository

import com.emil.domain.model.ReportRequest
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import retrofit2.Response

interface PeopleRepository {
    suspend fun findByUsername (token:String, startsWith:String):Response<List<UserResponse>>
    suspend fun findByLink (token:String, startsWith:String):Response<List<UserResponse>>
    suspend fun getUserPageData (token:String,id:Long):Response<UserPageDataResponse>
    suspend fun subscribe (token:String,id:Long):Response<Unit>
    suspend fun unsubscribe (token:String,id:Long):Response<Unit>
    suspend fun getUserSubscribers (token:String):Response<List<UserResponse>>
    suspend fun getUserSubscriptions (token:String):Response<List<UserResponse>>
    suspend fun getOutsiderUserSubscribers (id:Long):Response<List<UserResponse>>
    suspend fun getOutsiderSubscriptions (id:Long):Response<List<UserResponse>>
    suspend fun addToBlackList (token:String,userId:Long):Response<Unit>
    suspend fun removeFromBlackList (token:String,userId:Long):Response<Unit>
    suspend fun sendReport (token: String,report:ReportRequest):Response<Unit>

}